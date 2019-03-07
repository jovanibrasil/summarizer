package summ.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Summary;
import summ.model.Text;
import summ.ai.fuzzy.FuzzySystem;
import summ.ai.fuzzy.optimization.Optimization;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationTypes;
import summ.nlp.features.Features;
import summ.nlp.preprocesing.OperationTypes;
import summ.nlp.preprocesing.Preprocessing;
import summ.nlp.preprocesing.SentenceSegmentation;
import summ.nlp.preprocesing.StopWords;
import summ.nlp.preprocesing.Tokenization;
import summ.utils.Tuple;
import summ.utils.Utils;

public class Main {

	public static Text preProcessing(Text text, List<OperationTypes> preProcessingOperations) {
		
		if(text != null) {
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
	
	public static ArrayList<Tuple<Integer, Double>> computeSentencesInformativity(Text text){
		ArrayList<Tuple<Integer, Double>> outList = new ArrayList<Tuple<Integer,Double>>();
		// Fuzzy system integration
		FuzzySystem fs = new FuzzySystem("flc/fb2015.flc");
				
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				
				Tuple<String, Double> tfIsf = new Tuple<>("k1", (Double)s.getFeature("tf-isf")); 
				Tuple<String, Double> locLen = new Tuple<>("loc_len", (Double)s.getFeature("loc-len")); 
				Tuple<String, Double> titleWords = new Tuple<>("k2", (Double)s.getFeature("title-words-relative")); 
				
				List<Tuple<String, Double>> inputVariables = new ArrayList<>(Arrays.asList(tfIsf, locLen, titleWords));
				Double out = fs.run(inputVariables, "informatividade");
				outList.add(new Tuple<>(s.getId(), out));
			});
		});
		Collections.sort(outList);
		return outList;
	}
	
	public static void main(String[] args) {
		
		Text text = Utils.loadText("full-texts/ce94ab10-a.txt");
		
		List<OperationTypes> preProcessingOperations = new ArrayList<OperationTypes>(Arrays.asList(
				OperationTypes.PARAGRAPH_SEGMENTATION, OperationTypes.TOKENIZATION,
				//OperationTypes.NER, OperationTypes.POS,
				OperationTypes.TO_LOWER_CASE, OperationTypes.REMOVE_PUNCTUATION,
				OperationTypes.REMOVE_STOPWORDS, OperationTypes.IDENTIFY_TITLES));
		
		text = preProcessing(text, preProcessingOperations);		
		text = featureComputation(text);
		
		ArrayList<Tuple<Integer, Double>> outList = computeSentencesInformativity(text);
	
		// Generate the summary
		Text generatedSummary = new Text("");
		Paragraph paragraph = new Paragraph(""); // TODO where is the full paragraph text? Is it necessary?
		//int summarySize = (int)(0.3 * text.getTotalSentence());
		int summarySize = 11;
		int count = 0;
		for (Tuple<Integer, Double> t : outList) {
			Sentence sentence = text.getSentenceById(t.x);
			
			// Title sentences are ignored
			if(sentence.isTitle())
				continue;
			System.out.println("Sentence " + count + "\tinf="+t.y + "\n" + sentence.toString(Arrays.asList("tf-isf", "loc-len", "title-words-relative")));
			paragraph.addSentence(sentence);
			if(count == summarySize) {
				break;
			}
			count++;
		}
		
		generatedSummary.addParagraph(paragraph);
		//System.out.println(generatedSummary);
		
		// Load reference summary
		Text referenceSummary = Utils.loadText("summaries/reference/automatic/ce94ab10-a_reference1.txt");
		preProcessingOperations = new ArrayList<OperationTypes>(Arrays.asList(
				OperationTypes.PARAGRAPH_SEGMENTATION, OperationTypes.TOKENIZATION));
		Preprocessing pp = new Preprocessing(referenceSummary, preProcessingOperations);
		
		// Evaluation
		Evaluation.evaluate(generatedSummary, referenceSummary, EvaluationTypes.OVERLAP);
		Evaluation.evaluate(generatedSummary, referenceSummary, EvaluationTypes.ROUGE);
		
		
		// Optimization
		
//		String[] varNames = { "k1", "k2", "loc_len" };
//		Optimization optmization = new Optimization("flc/fb2015.flc", varNames);
		
	}

}
