package summ.summarizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.FuzzySystem;
import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.nlp.evaluation.EvaluationMethod;
import summ.nlp.evaluation.EvaluationResult;
import summ.settings.GlobalSettings;
import summ.settings.SummarizationSettings;
import summ.settings.SummarizationSettings.SummarizationType;
import summ.utils.CustomFileUtils;
import summ.utils.ExportCSV;
import summ.utils.ExportHTML;
import summ.utils.Pipeline;
import summ.utils.Tuple;
import summ.utils.Utils;
import summ.utils.XmlUtils;

public class Summarizer {

	private static final Logger log = LogManager.getLogger(Summarizer.class);
	private GlobalSettings globalSettings;
	public SummarizationSettings summarizationSettings;

	public Summarizer(GlobalSettings globalSettings) {
		this.globalSettings = globalSettings;
		this.summarizationSettings = new SummarizationSettings(this.globalSettings.SUMMARIZATION_PROPERTIES_PATH);
	}

	public ArrayList<Tuple<Integer>> computeSentencesInformativity(Text text, FuzzySystem fs, List<String> variables) {

		ArrayList<Tuple<Integer>> outList = new ArrayList<Tuple<Integer>>();
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				
				for (int index = 0; index < variables.size() - 1; index++) {
					String variableName = variables.get(index);
					fs.setInputVariable(variableName, (double) s.getFeature(variableName));
				}
				fs.setOutputVariable(variables.get(variables.size() - 1));
				//System.out.println("[" + s.getId() + "] " + s.getInitialValue());
				
				boolean showResult = s.getId() == 5 ? false : false;
				
				double score = fs.evaluate(showResult).getValue();
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
		List<Sentence> sentences = new ArrayList<Sentence>();
		for (Tuple<Integer> t : outList) {

			Sentence sentence = text.getSentenceById(t.x);
			if (sentence.isTitle())// Title sentences are ignored
				continue;

			// counting words
			if (count + sentence.wordCounter > summarySize)
				break;

			sentences.add(sentence);
			count += sentence.wordCounter;

			// counting sentences
//			if (count == summarySize) {
//				break;
//			}
//			count++;
		}

		Collections.sort(sentences);
		paragraph.addSentences(sentences);
		generatedSummary.addParagraph(paragraph);
		return generatedSummary;
	}

	/**
	 * Evaluates the summary with the method defined in the settings file.
	 * 
	 * @param generatedSummary
	 * @return the evaluation result (EvaluationResult type)
	 */
	public EvaluationResult evaluateSummary(EvaluationMethod evaluationMethod, Text generatedSummary) {
		if (evaluationMethod != null) {
			log.debug("Evaluating the summary ...");
			Text referenceSummary = CustomFileUtils.loadText(this.summarizationSettings.AUTO_SUMMARIES_PATH
					+ generatedSummary.getName().replace(".txt", "") + "_areference1.txt");
			referenceSummary = summarizationSettings.SUMMARY_PREPROCESSING_PIPELINE.process(referenceSummary);

			// save the rouge configuration file
			XmlUtils.createConfigurationFile(this.summarizationSettings.MANUAL_SUMMARIES_PATH,
					this.globalSettings.OUTPUT_PATH, generatedSummary.getName(), this.globalSettings.OUTPUT_PATH);

			EvaluationResult evaluationResult = evaluationMethod.evaluate(generatedSummary, referenceSummary, this.globalSettings.OUTPUT_PATH);
			evaluationResult.setEvalName(generatedSummary.getName());
			generatedSummary.setEvaluationResult(evaluationResult);
			
			/*
			 * Exports the reference summary with selected sentences marked with underline.
			 */
			String refSentences = "";
			// save extra formats
			for (Sentence s : referenceSummary.getSentences()) {
				refSentences += generatedSummary.containsSentence(s) ? " \\uline{" + s.getInitialValue() + "} "
						: s.getInitialValue();
			}
			refSentences += "\n";

			CustomFileUtils.saveListOfObjects(Arrays.asList(refSentences),
					this.globalSettings.OUTPUT_PATH + "/sumarioreferencia_marcado.txt");

			log.debug(evaluationResult);
			return evaluationResult;
		}
		return null;
	}

	public EvaluationResult evaluateSummary(EvaluationMethod evaluationMethod, Text generatedSummary,
			Text referenceSummary) {
		if (evaluationMethod != null) {
			log.debug("Evaluating the summary ...");
			EvaluationResult evaluationResult = evaluationMethod.evaluate(generatedSummary, referenceSummary, this.globalSettings.OUTPUT_PATH);
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
		double summaryPercentual = summarizationSettings.SUMMARY_SIZE_PERCENTUAL;
		if (summaryPercentual > 0) {
			return (int) (summaryPercentual * maxSentences);
		} else {
			return maxSentences;
		}
	}

	public void saveResult(Text text1, String fileName) {
		switch (summarizationSettings.OUTPUT_TYPE) {
		case NONE:
			return;
		case CONSOLE:
			log.info(text1);
			break;
		case FEATURES_BY_SENTENCE_CSV:
			ExportCSV.exportSentenceFeatures(text1, this.globalSettings.OUTPUT_PATH, fileName);
			break;
		case FEATURES_BY_SENTENCE_HTML:
			ExportHTML.exportSentecesAndFeatures(text1, summarizationSettings.OUTPUT_EXPORT_VARIABLES,
					this.globalSettings.OUTPUT_PATH, fileName);
			break;
		default:
			break;
		}
	}

	public void saveResult(Text text1, Text text2) {
		switch (summarizationSettings.OUTPUT_TYPE) {
		case NONE:
			return;
		case HIGHLIGHTED_TEXT:
			ExportHTML.exportHighlightText(text1, text2.getSentencesMap(), this.globalSettings.OUTPUT_PATH);
			break;
		case OVERLAPPED_SENTENCES_HTML:
			ExportHTML.exportOverlappedFeatures(text1, text2, this.globalSettings.OUTPUT_PATH);
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
			summarizationSettings.FEATURES_PIPELINE
					.process(summarizationSettings.TEXT_PREPROCESSING_PIPELINE.process(text));
			summarizationSettings.TEXT_PREPROCESSING_PIPELINE.process(text.getReferenceSummary());
		}
	}

	public Text summarizeText(Text text, List<String> varNames, boolean saveResults) {
		// log.info("Summarizing " + textPath + " ...");
		// Load, pre-process and compute the features of a complete text
		//Text text = FileUtils.loadText(textPath);
		text = prepareText(summarizationSettings.TEXT_PREPROCESSING_PIPELINE, summarizationSettings.FEATURES_PIPELINE,
				text);
		
		// Summary generation
		FuzzySystem fs = new FuzzySystem(this.summarizationSettings.FUZZY_SYSTEM_PATH);

		Text generatedSummary = summarizeText(text, fs, varNames, saveResults);

		if(saveResults) {
			this.saveResult(text, "full-text");

			/*
			 * Gera texto original marcado com sentenças selecionadas no sumário gerado.
			 * 
			 */
			String originalText = "";
			int id = 0;
			for (Paragraph p : text.getParagraphs()) {
				for (Sentence s : p.getSentences()) {
					originalText += generatedSummary.containsSentence(s)
							? " \\uline{[" + id++ + "] " + s.getInitialValue() + "}"
							: s.getInitialValue();
				}
				originalText += "\\\\";
			}
			CustomFileUtils.saveListOfObjects(Arrays.asList(originalText),
					this.globalSettings.OUTPUT_PATH + "/textooriginal_marcado.txt");
		}
		return generatedSummary;
	}

	
	public Text summarizeText(Text text, FuzzySystem fs, List<String> varNames, boolean saveResults) {
		log.debug("Summarizing " + text.getFullTextPath() + " ...");
		// Compute sentences informativity using fuzzy system
		//long startTime = System.nanoTime();
		// int summarySize = this.getSummarySize(text.getTotalSentence());
		int summarySize = (int) (text.wordCounter * 0.3);
		ArrayList<Tuple<Integer>> sentencesInformativity = this.computeSentencesInformativity(text, fs, varNames);
		Text generatedSummary = this.generateSummary(text, summarySize, sentencesInformativity);
		
		// save result
		if(saveResults) ExportHTML.exportSummaryRougeFormat(generatedSummary, this.globalSettings.OUTPUT_PATH);
		this.evaluateSummary(summarizationSettings.EVALUATION_METHOD, generatedSummary);
		
		//long timeElapsed = (System.nanoTime() - startTime) / 1000000;
		//log.debug("Summarization time (ms) : " + timeElapsed );
		return generatedSummary;
	}

	public List<Text> summarizeTexts(List<Text> texts) {
		List<Text> generatedSummaries = new ArrayList<>();
		List<EvaluationResult> evaluationResults = new ArrayList<>();
		for (Text entry : texts) {
			log.info("Processing " + entry.getName() + " ...");
			Text generatedSummary = this.summarizeText(entry, summarizationSettings.VAR_NAMES, true);
			generatedSummaries.add(generatedSummary);
			// evaluate
			EvaluationResult result = this.evaluateSummary(summarizationSettings.EVALUATION_METHOD, generatedSummary);
			evaluationResults.add(result);
		}
		// save evaluation results
		ExportCSV.saveEvaluationResult(evaluationResults, this.globalSettings.OUTPUT_PATH);
		return generatedSummaries;
	}
	
	public void run() {


		if (summarizationSettings.SUMMARIZATION_TYPE.equals(SummarizationType.SINGLE)) {

			globalSettings.OUTPUT_PATH = "results/summ_" + Utils.generateStringFormattedData();
			CustomFileUtils.createDir(globalSettings.OUTPUT_PATH);
			
			// Run a single text summarization
			
			Text text =  CustomFileUtils.loadText(this.summarizationSettings.FULL_TEXTS_PATH + this.summarizationSettings.TEXT_NAME);
			Text generatedSummary = this.summarizeText(text, summarizationSettings.VAR_NAMES, true);
			log.info(generatedSummary.getEvaluationResult());
			
			this.saveResult(generatedSummary, "summ-text");

			/*
			 * Generated a file with summary sentences (numbered).
			**/

			String numeredSentences = "";
			int id = 0;
			for (Sentence s : generatedSummary.getSentences()) {
				numeredSentences += " [" + id++ + "] " + s.getInitialValue();
			}
			numeredSentences += "\n";

			CustomFileUtils.saveListOfObjects(Arrays.asList(numeredSentences), this.globalSettings.OUTPUT_PATH + "/sumariogerado_numerado.txt");

		} else {
			
			// if samples are pre-defined
			if(this.summarizationSettings.samples.size() > 0) {
				
				for (int i = 0; i < this.summarizationSettings.samples.size(); i++) {
						
					globalSettings.OUTPUT_PATH = "results/summ_" + Utils.generateStringFormattedData() + "_set" + i;
					CustomFileUtils.createDir(globalSettings.OUTPUT_PATH);

					List<Text> sample = CustomFileUtils.loadTexts(this.summarizationSettings.FULL_TEXTS_PATH, 
							this.globalSettings.MANUAL_SUMMARIES_PATH, this.summarizationSettings.samples.get(i));
					
					this.summarizeTexts(sample);
					
				}
				
			}else {
				// Run an specified list of summaries
				globalSettings.OUTPUT_PATH = "results/summ_" + Utils.generateStringFormattedData();
				CustomFileUtils.createDir(globalSettings.OUTPUT_PATH);
				int corpusSize = this.summarizationSettings.SAMPLE_SIZE;
				// if no samples are pre-defined: randomly load files 
				if(this.summarizationSettings.SAMPLE_SIZE == 0) { // load all files
					corpusSize = CustomFileUtils.countFiles(this.summarizationSettings.FULL_TEXTS_PATH);
				}	
				List<List<Text>> data = CustomFileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.MANUAL_SUMMARIES_PATH, corpusSize, 0);				
				this.summarizeTexts(data.get(0));
			}	
			
		}

	}

}
