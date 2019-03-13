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

import com.opencsv.CSVWriter;

import summ.ai.fuzzy.FuzzySystem;
import summ.ai.fuzzy.optimization.Optimization;
import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationTypes;
import summ.nlp.features.Features;
import summ.nlp.preprocesing.OperationTypes;
import summ.nlp.preprocesing.Preprocessing;
import summ.utils.Tuple;
import summ.utils.Utils;

public class Summarizer {

	private static List<OperationTypes> preProcessingOperations = new ArrayList<OperationTypes>(
			Arrays.asList(OperationTypes.SENTENCE_SEGMENTATION, OperationTypes.TOKENIZATION));
	
	public static Text preProcessing(Text text, List<OperationTypes> preProcessingOperations) {

		if (text != null) {
			Preprocessing pp = new Preprocessing(text, preProcessingOperations);
		}
		return text;
	}

	public static Text featureComputation(Text text) {
		// eliminar linhas vazias
		// eliminar espaços antes e depois dos termos
		// tratar títulos
		Features f = new Features(text);
		// TODO seletor do pipeline de features
		return text;
	}

	public static ArrayList<Tuple<Integer, Double>> computeSentencesInformativity(Text text, FuzzySystem fs) {
		ArrayList<Tuple<Integer, Double>> outList = new ArrayList<Tuple<Integer, Double>>();
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {

				Tuple<String, Double> tfIsf = new Tuple<>("k1", (Double) s.getFeature("tf-isf"));
				Tuple<String, Double> locLen = new Tuple<>("loc_len", (Double) s.getFeature("loc-len"));
				Tuple<String, Double> titleWords = new Tuple<>("k2", (Double) s.getFeature("title-words-relative"));

				List<Tuple<String, Double>> inputVariables = new ArrayList<>(Arrays.asList(tfIsf, locLen, titleWords));
				Double out = fs.run(inputVariables, "informatividade");
				outList.add(new Tuple<>(s.getId(), out));
			});
		});
		Collections.sort(outList);
		return outList;
	}

	public static Text generateSummary(Text text, int summarySize, FuzzySystem fs) {
		// Compute sentences informativity using fuzzy system
		ArrayList<Tuple<Integer, Double>> outList = computeSentencesInformativity(text, fs);
		// Generate the summary
		Text generatedSummary = new Text("");
		Paragraph paragraph = new Paragraph(""); // TODO where is the full paragraph text? Is it necessary?
		int count = 0;
		for (Tuple<Integer, Double> t : outList) {
			Sentence sentence = text.getSentenceById(t.x);
			// Title sentences are ignored
			if (sentence.isTitle())
				continue;
			paragraph.addSentence(sentence);
			if (count == summarySize) {
				break;
			}
			count++;
		}
		generatedSummary.addParagraph(paragraph);
		return generatedSummary;
	}

	public static Text summarize(Text text, int summarySize) {

		List<OperationTypes> preProcessingOperations = new ArrayList<OperationTypes>(
				Arrays.asList(OperationTypes.SENTENCE_SEGMENTATION,
						// OperationTypes.NER, OperationTypes.POS,
						OperationTypes.TO_LOWER_CASE, OperationTypes.REMOVE_PUNCTUATION, OperationTypes.TOKENIZATION,
						OperationTypes.IDENTIFY_TITLES, OperationTypes.REMOVE_STOPWORDS));

		// Pre-processing
		text = preProcessing(text, preProcessingOperations);
		text = featureComputation(text);

		// Summary generation
		FuzzySystem fs = new FuzzySystem("flc/fb2015.flc");
		
		// int summarySize = (int)(0.3 * text.getTotalSentence());
		Text generatedSummary = generateSummary(text, summarySize, fs);
		
		return generatedSummary;
	}
	
	public static void summarizationText(String fileName) {
		// Load complete text
		Text text = Utils.loadText("projects/temario-2014/full-texts/", fileName);
		Text referenceSummary = Utils.loadText("projects/temario-2014/summaries/"
					+ "reference/automatic/", "po96fe09-b_areference1.txt");
		
		
		Preprocessing pp = new Preprocessing(referenceSummary, preProcessingOperations);
		

		int summarySize = referenceSummary.getTotalSentence();
		summarize(text, summarySize);
		
		// Evaluation methods
//		return Evaluation.evaluate(generatedSummary, 
//				referenceSummary, EvaluationTypes.OVERLAP);
//		
		
	}

	public static void summarizeTexts() {
		
		HashMap<String, Text> texts = Utils.loadTexts("projects/temario-2014/full-texts/", null);
		HashMap<String, Text> refSummaries = Utils.loadTexts("projects/temario-2014/summaries/"
				+ "reference/automatic/", null);
		
		try {
			
			File file = new File("results/texts-evaluation-"+(new Date()).toString());  
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
			
			for (Entry<String, Text> entry : texts.entrySet()) {
				
				System.out.println("Processing " + entry.getValue().getName() + " ...");
				
				Text referenceSummary = refSummaries.get(entry.getKey());
				Text originalText = entry.getValue();
				
				Preprocessing pp = new Preprocessing(referenceSummary, preProcessingOperations);
				
				// Summarize
				int summarySize = referenceSummary.getTotalSentence();
				Text generatedSummary = summarize(originalText, summarySize);
				
				// Evaluate generated summary
				HashMap<String, HashMap<String, Object>> result = Evaluation.evaluate(generatedSummary,
						referenceSummary, EvaluationTypes.OVERLAP);	
				
				// Save summarization result
				String[] data = { entry.getValue().getName(), result.get("result").get("total_sentences").toString(),
						result.get("result").get("overlap").toString() };
				writer.writeNext(data);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void fuzzyOptimization() {

		// Load complete text
		Text text = Utils.loadText("projects/temario-2014/full-texts/", "ce94ab10-a.txt");

		List<OperationTypes> preProcessingOperations = new ArrayList<OperationTypes>(
				Arrays.asList(OperationTypes.SENTENCE_SEGMENTATION,
						// OperationTypes.NER, OperationTypes.POS,
						OperationTypes.TO_LOWER_CASE, OperationTypes.REMOVE_PUNCTUATION, OperationTypes.TOKENIZATION,
						OperationTypes.IDENTIFY_TITLES, OperationTypes.REMOVE_STOPWORDS));

		// Pre-processing
		text = preProcessing(text, preProcessingOperations);
		text = featureComputation(text);

		// Load reference summary
		Text referenceSummary = Utils.loadText("summaries/reference/automatic/", 
				"ce94ab10-a_reference1.txt");
		Preprocessing pp = new Preprocessing(referenceSummary, preProcessingOperations);

		// Optimization
		String[] varNames = { "k1", "k2", "loc_len", "informatividade" };
		Optimization optmization = new Optimization("flc/fb2015.flc", varNames, text, referenceSummary);

	}
	
}
