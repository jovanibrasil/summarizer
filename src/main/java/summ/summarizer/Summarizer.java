package summ.summarizer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.FuzzySystem;
import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.nlp.evaluation.EvaluationResult;
import summ.settings.SummarizationSettings;
import summ.settings.SummarizationSettings.SummarizationType;
import summ.settings.SummarizerSettings;
import summ.utils.ExportCSV;
import summ.utils.ExportHTML;
import summ.utils.Pipeline;
import summ.utils.Tuple;
import summ.utils.FileUtils;

public class Summarizer {
	
	private static final Logger log = LogManager.getLogger(Summarizer.class);
	private SummarizerSettings summarizerSettings;
	public SummarizationSettings settings;
	
	public Summarizer(SummarizerSettings summarizerSettings) {
		this.summarizerSettings = summarizerSettings;
		this.settings = new SummarizationSettings(this.summarizerSettings.SUMMARIZATION_PROPERTIES_PATH);
	}
	
	public ArrayList<Tuple<Integer>> computeSentencesInformativity(Text text, FuzzySystem fs, List<String> variables) {
		
		ArrayList<Tuple<Integer>> outList = new ArrayList<Tuple<Integer>>();
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
		
				for (int index = 0; index < variables.size()-1; index++) {
					String variableName = variables.get(index);
					fs.setInputVariable(variableName, (double)s.getFeature(variableName));
				}
				fs.setOutputVariable(variables.get(variables.size()-1));
				
				double score = fs.evaluate().getValue(); 
				s.setScore(score);
				outList.add(new Tuple<>(s.getId(), score));
				
			});
		});
		Collections.sort(outList);
		return outList;
	}

	public Text generateSummary(Text text, int summarySize, ArrayList<Tuple<Integer>> outList) {
		log.debug("Generating text summary with summary size " + summarySize);
		// Generate the summary
		Text generatedSummary = new Text("");
		generatedSummary.setName(text.getName());
		Paragraph paragraph = new Paragraph(""); // TODO where is the full paragraph text? Is it necessary?
		int count = 0;
		for (Tuple<Integer> t : outList) {

			Sentence sentence = text.getSentenceById(t.x);
			// Title sentences are ignored
			if (sentence.isTitle())
				continue;
			paragraph.addSentence(sentence);
			//log.info("Sentence " + t.x + " with score " + t.y + " was selected");
			if (count == summarySize) {
				break;
			}
			count++;
		}
		generatedSummary.addParagraph(paragraph);
		return generatedSummary;
	}

	/**
	 * Evaluates the summary with the method define in the settings file.
	 * 
	 * @param generatedSummary
	 * @return
	 */
	public EvaluationResult evaluateSummary(Text generatedSummary) {
		if(settings.EVALUATION_METHOD != null) {
			log.debug("Evaluating the summary ...");
			Text referenceSummary = FileUtils.loadText(this.settings.AUTO_SUMMARIES_PATH + 
					generatedSummary.getName().replace(".txt", "") + "_areference1.txt");
			referenceSummary = settings.SUMMARY_PREPROCESSING_PIPELINE.process(referenceSummary);
			EvaluationResult evaluationResult = settings.EVALUATION_METHOD.evaluate(generatedSummary, referenceSummary);
			evaluationResult.setEvalName(generatedSummary.getName());
			generatedSummary.setEvaluationResult(evaluationResult);
			log.debug(evaluationResult);
			return evaluationResult;
		}
		return null;
	}
	
	public EvaluationResult evaluateSummary(Text generatedSummary, Text referenceSummary) {
		if(settings.EVALUATION_METHOD != null) {
			log.debug("Evaluating the summary ...");
			EvaluationResult evaluationResult = settings.EVALUATION_METHOD.evaluate(generatedSummary, referenceSummary);
			evaluationResult.setEvalName(generatedSummary.getName());
			generatedSummary.setEvaluationResult(evaluationResult);
			log.debug(evaluationResult);
			return evaluationResult;
		}
		return null;
	}
	
	/**
	 * Calculates the size of the summary based on maximum sentences permitted and
	 * the percent value defined in the settings file. 
	 * 
	 * @param maxSentences is the maximum of sentences permitted
	 * @return the summary size
	 */
	public int getSummarySize(int maxSentences) {
		double summaryPercentual = settings.SUMMARY_SIZE_PERCENTUAL;
		if(summaryPercentual > 0) {
			return (int)(summaryPercentual * maxSentences);
		}else {
			return maxSentences;
		}
	}
	
	public void saveResult(Text text1) {
		switch (settings.OUTPUT_TYPE) {
			case NONE:
				return;
			case CONSOLE:
				log.info(text1);
				break;
			case FEATURES_BY_SENTENCE_CSV:
				ExportCSV.exportSentenceFeatures(text1, this.summarizerSettings.OUTPUT_PATH);
				break;
			case FEATURES_BY_SENTENCE_HTML:
				ExportHTML.exportSentecesAndFeatures(text1, settings.OUTPUT_EXPORT_VARIABLES, this.summarizerSettings.OUTPUT_PATH);
				break;
			default:
				break;
		}
	}
	
	public void saveResult(Text text1, Text text2) {
		switch (settings.OUTPUT_TYPE) {
			case NONE:
				return;
			case HIGHLIGHTED_TEXT:
				ExportHTML.exportHighlightText(text1, text2.getSentencesMap(), this.summarizerSettings.OUTPUT_PATH);
				break;
			case OVERLAPPED_SENTENCES_HTML:
				ExportHTML.exportOverlappedFeatures(text1, text2, this.summarizerSettings.OUTPUT_PATH);
				break;
			default:
				break;
		}
	}
	
	public Text prepareText(Pipeline<Text> preprocessingPipeline, Pipeline<Text> featuresPipeline, Text text) {
		return featuresPipeline.process(preprocessingPipeline.process(text));
	}
	
	public void prepareTextList(List<Text> textList) {
		for (Text text : textList) {
			settings.FEATURES_PIPELINE.process(settings.TEXT_PREPROCESSING_PIPELINE.process(text));
			settings.TEXT_PREPROCESSING_PIPELINE.process(text.getReferenceSummary());
		}
	}
	
	public Text summarizeText(String textPath, List<String> varNames) {
		log.info("Summarizing " + textPath + " ...");
		// Load, pre-process and compute the features of a complete text
		Text text = FileUtils.loadText(textPath);
		text = prepareText(settings.TEXT_PREPROCESSING_PIPELINE, settings.FEATURES_PIPELINE, text);
		this.saveResult(text);
		// Summary generation
		FuzzySystem fs = new FuzzySystem(this.settings.FUZZY_SYSTEM_PATH);
		// Compute sentences informativity using fuzzy system
		int summarySize = this.getSummarySize(text.getTotalSentence());
		ArrayList<Tuple<Integer>> sentencesInformativity = computeSentencesInformativity(text, fs, varNames);		
		return generateSummary(text, summarySize, sentencesInformativity);
	}

	public Text summarizeText(Text text, FuzzySystem fs, List<String> varNames) {
		log.debug("Summarizing " + text.getFullTextPath() + " ...");
		// Compute sentences informativity using fuzzy system
		int summarySize = this.getSummarySize(text.getTotalSentence());
		ArrayList<Tuple<Integer>> sentencesInformativity = computeSentencesInformativity(text, fs, varNames);		
		return generateSummary(text, summarySize, sentencesInformativity);
	}
	
	public List<Text> summarizeTexts(List<Path> texts) {
		List<Text> generatedSummaries = new ArrayList<>();
		for (Path entry : texts) {
			log.info("Processing " + entry.getFileName() + " ...");
			Text generatedSummary = this.summarizeText(this.settings.FULL_TEXTS_PATH + entry.getFileName(), this.settings.VAR_NAMES);
			generatedSummaries.add(generatedSummary);
		}
		return generatedSummaries;
	}

	public void run() {
		
		String fileName = "results/summ_" + (new Date().toString()).replace("-", "_").replace(" ", "_").replace(":", "_");
	    FileUtils.createDir(fileName);
	    summarizerSettings.OUTPUT_PATH = fileName;
		
		if(settings.SUMMARIZATION_TYPE.equals(SummarizationType.SINGLE)) {
			// Run a single text summarization
			Text generatedSummary = this.summarizeText(this.settings.FULL_TEXTS_PATH + this.settings.TEXT_NAME, settings.VAR_NAMES);
			this.evaluateSummary(generatedSummary);
			this.saveResult(generatedSummary);
		}else {
			// Run an specified list of summaries
			List<Path> texts = FileUtils.listTexts(this.settings.FULL_TEXTS_PATH);
			this.summarizeTexts(texts);
		}
		
	}
		
}
