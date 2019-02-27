package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ai.fuzzy.FuzzySystem;
import model.Paragraph;
import model.Sentence;
import model.Summary;
import model.Text;
import nlp.evaluation.Evaluation;
import nlp.evaluation.EvaluationTypes;
import nlp.features.Features;
import nlp.preprocesing.OperationTypes;
import nlp.preprocesing.Preprocessing;
import nlp.preprocesing.SentenceSegmentation;
import nlp.preprocesing.StopWords;
import nlp.preprocesing.Tokenization;
import utils.Tuple;
import utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		Text text = Utils.loadText("full-texts/ce94ab10-a.txt");
		
		List<OperationTypes> operations = new ArrayList<OperationTypes>(Arrays.asList(
				OperationTypes.PARAGRAPH_SEGMENTATION, OperationTypes.SENTENCE_TOKENIZATION,
				OperationTypes.TO_LOWER_CASE, OperationTypes.REMOVE_PUNCTUATION,
				OperationTypes.REMOVE_STOPWORDS));
		
		
		if(text != null) {
			Preprocessing pp = new Preprocessing(text, operations);
		}
		
		// eliminar linhas vazias
		// eliminar espaços antes e depois dos termos
		Features f = new Features(text);
		// TODO seletor do pipeline de features
		
		// Fuzzy system integration
		FuzzySystem fs = new FuzzySystem("flc/fb2015.flc");
		
		ArrayList<Tuple<Integer, Double>> outList = new ArrayList<>();
		
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
		
		// Generate the summary
		Text generatedSummary = new Text("");
		Paragraph paragraph = new Paragraph(""); // TODO where is the full paragraph text? Is it necessary?
		int summarySize = (int)(0.3 * text.getTotalSentence());
		int count = 0;
		for (Tuple<Integer, Double> t : outList) {
			Sentence sentence = text.getSentenceById(t.x);
			paragraph.addSentence(sentence);
			if(count == summarySize) {
				break;
			}
			count++;
		}
		generatedSummary.addParagraph(paragraph);
		System.out.println(generatedSummary);
		
		
		Text referenceSummary = Utils.loadText("summaries/reference/automatic/ce94ab10-a.0.ref.txt");
		operations = new ArrayList<OperationTypes>(Arrays.asList(
				OperationTypes.PARAGRAPH_SEGMENTATION, OperationTypes.SENTENCE_TOKENIZATION));
		Preprocessing pp = new Preprocessing(referenceSummary, operations);
		
		// avaliar
		//Evaluation evaluation = new Evaluation();
		Evaluation.evaluate(generatedSummary, referenceSummary, EvaluationTypes.OVERLAP);
		
		// otimizações
		
		
	}

}
