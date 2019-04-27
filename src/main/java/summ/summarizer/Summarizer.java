package summ.summarizer;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.opencsv.CSVWriter;

import summ.fuzzy.FuzzySystem;
import summ.fuzzy.optimization.Optimization;
import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationResult;
import summ.nlp.evaluation.SentenceOverlap;
import summ.nlp.features.FeatureType;
import summ.nlp.features.Frequency;
import summ.nlp.features.Length;
import summ.nlp.features.LocLen;
import summ.nlp.features.Location;
import summ.nlp.features.TextRank;
import summ.nlp.features.Title;
import summ.nlp.preprocesing.Lemmatizer;
import summ.nlp.preprocesing.Misc;
import summ.nlp.preprocesing.POSTagger;
import summ.nlp.preprocesing.PreProcessingTypes;
import summ.nlp.preprocesing.SentenceSegmentation;
import summ.nlp.preprocesing.StopWords;
import summ.nlp.preprocesing.Titles;
import summ.nlp.preprocesing.Tokenization;
import summ.utils.ExportCSV;
import summ.utils.ExportHTML;
import summ.utils.Pipeline;
import summ.utils.Tuple;
import summ.utils.Utils;

public class Summarizer {
	
	private static final Logger log = LogManager.getLogger(Summarizer.class);

	public static final String TEMARIO_AUTO_SUMMARIES_PATH = "projects/temario-2014/summaries/reference/automatic/";
	public static final String TEMARIO_FULL_TEXTS_PATH = "projects/temario-2014/full-texts/";
	public static final int SUMMARY_EVALUATION_LEN = 10;
	
	public static Pipeline<Text> getSummaryPreProcessingPipeline() {
		return new Pipeline<Text>(new SentenceSegmentation(), new Tokenization(PreProcessingTypes.NEURAL_TOKENIZATION));
	}

	public static Pipeline<Text> getTextPreProcessingPipeline() {
		return new Pipeline<Text>(new SentenceSegmentation(), new Misc(PreProcessingTypes.TO_LOWER_CASE),
				new Misc(PreProcessingTypes.REMOVE_PUNCTUATION), new Tokenization(PreProcessingTypes.NEURAL_TOKENIZATION),
				new Titles(), new StopWords(), new POSTagger(), new Lemmatizer(null));
	}
	
	public static Text featureComputation(Text text) {
		return new Pipeline<Text>(
				new Location(), new Length(), new LocLen(), new Frequency(), 
					new Title(), new TextRank(FeatureType.TFISF)).process(text);
		
	}

	public static ArrayList<Tuple<Integer>> computeSentencesInformativity(Text text, FuzzySystem fs, List<String> variables) {
		
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

	public static Text generateSummary(Text text, int summarySize, ArrayList<Tuple<Integer>> outList) {
		log.debug("Generating text summary with summary size " + summarySize);
		// Generate the summary
		Text generatedSummary = new Text("");
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

	public static Text summarize(Text text, int summarySize, String systemPath, List<String> varNames) {

		// Pre-processing
		text = getTextPreProcessingPipeline().process(text);
		text = featureComputation(text);
		
		// Summary generation
		FuzzySystem fs = new FuzzySystem(systemPath);
		//fs.setOutputVariable(outputVariableName);
		// Compute sentences informativity using fuzzy system
		ArrayList<Tuple<Integer>> sentencesInformativity = computeSentencesInformativity(text, fs, varNames);
		
		// int summarySize = (int)(0.3 * text.getTotalSentence());
		Text generatedSummary = generateSummary(text, summarySize, sentencesInformativity);
		
//		System.out.println(generatedSummary);
//		
//		System.out.println("-----------------------------------------");
//		System.out.println(text);
		
		return generatedSummary;
	}
	
	public static void summarizationText(String fileName, String systemPath, List<String> varNames) {
		// Load complete text
		Text text = Utils.loadText(TEMARIO_FULL_TEXTS_PATH, fileName);
		Text referenceSummary = Utils.loadText(TEMARIO_AUTO_SUMMARIES_PATH, fileName.replace(".txt", "") + "_areference1.txt");
		
		referenceSummary = getSummaryPreProcessingPipeline().process(referenceSummary);

		int summarySize = referenceSummary.getTotalSentence();
		Text generatedSummary = summarize(text, summarySize, systemPath, varNames);
		
		Evaluation so = new SentenceOverlap();
		EvaluationResult result = so.evaluate(generatedSummary, referenceSummary);	
		//log.info(result);
		ExportCSV.exportSentenceFeatures(text);
		ExportHTML.exportSentecesAndFeatures(text, Arrays.asList("relative-len", "relative-location", "tf-isf", "title-words-relative"));
		ExportHTML.exportHighlightText(text, generatedSummary.getSentencesMap());
		ExportHTML.exportOverlappedFeatures(generatedSummary, referenceSummary);
		
	}

	public static void summarizeTexts(String systemPath) {
		
		HashMap<String, Text> texts = Utils.loadTexts(TEMARIO_FULL_TEXTS_PATH, null);
		HashMap<String, Text> refSummaries = Utils.loadTexts(TEMARIO_AUTO_SUMMARIES_PATH, null);
		
		List<String> varNames = Arrays.asList("tf_isf", "title_words_relative", "loc_len", "informatividade");
		
		try {
			
			File file = new File("results/texts-evaluation-"+(new Date()).toString());  
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);

	        String[] header = new String[] { "textName", "precision", "recall", "fMeasure", "retrievedSentences", "relevantSentences", "correctSentences" };
	        writer.writeNext(header);
	        
			for (Entry<String, Text> entry : texts.entrySet()) {
				
				//System.out.println("Processing " + entry.getValue().getName() + " ...");
				
				Text referenceSummary = refSummaries.get(entry.getKey());
				Text originalText = entry.getValue();
				
				referenceSummary = getSummaryPreProcessingPipeline().process(referenceSummary);
				
				// Summarize
				int summarySize = referenceSummary.getTotalSentence();
				Text generatedSummary = summarize(originalText, summarySize, systemPath, varNames);
				
				// Evaluate generated summary
				
				Evaluation so = new SentenceOverlap();
				EvaluationResult result = so.evaluate(generatedSummary, referenceSummary);	
				result.setEvalName(entry.getValue().getName());
				
				System.out.println(result);
				
				// Save summarization result
				String[] data = { entry.getValue().getName(), result.getMetric("precision").toString(), result.getMetric("recall").toString(), 
					result.getMetric("fMeasure").toString(), result.getMetric("retrievedSentences").toString(), result.getMetric("relevantSentences").toString(),
						result.getMetric("correctSentences").toString() };
				
				//writer.writeNext(data);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void fuzzyOptimization() {
	
		List<Text> texts = Utils.loadTexts(TEMARIO_FULL_TEXTS_PATH, null, SUMMARY_EVALUATION_LEN).stream().map(text -> {
			Text preProcessedText = getTextPreProcessingPipeline().process(text);
			return featureComputation(preProcessedText);
		}).collect(Collectors.toList());
		
		List<Text> refSummaries = texts.stream().map(text -> { 
			Text referenceSummary = Utils.loadText(TEMARIO_AUTO_SUMMARIES_PATH, text.getName().replace(".", "_areference1."));
			return getSummaryPreProcessingPipeline().process(referenceSummary);
		}).collect(Collectors.toList());
		
		// Optimization
		//List<String> varNames =  Arrays.asList( "tf_isf", "title_words_relative", "loc_len", "informatividade" );
		List<String> varNames = Arrays.asList("tf_isf", "informatividade"); 
		Evaluation evaluationMethod = new SentenceOverlap();
		//Optimization optimization = new Optimization("fcl/fb2015.fcl", text, referenceSummary, evaluationMethod, varNames);
		Optimization optmization = new Optimization("fcl/simplek1.fcl", texts, refSummaries, evaluationMethod, varNames);
		optmization.run();
		
	}
		
}
