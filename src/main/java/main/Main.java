package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import features.Features;
import fuzzy.FuzzySystem;
import model.Text;
import nlp.preprocesing.Preprocessing;

import utils.Utils;
import utils.Tuple;

public class Main {

	public static void main(String[] args) {
		
		Text text = Utils.readFile("ce94ab10-a.txt");
		
		if(text != null) {
			Preprocessing pp = new Preprocessing(text);
		}
		
		// eliminar linhas vazias
		// eliminar espaços antes e depois dos termos
		Features f = new Features(text);
		// TODO seletor do pipeline de features
		
		// Fuzzy system integration
		FuzzySystem fs = new FuzzySystem("flc/fb2015.flc");
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				
				Tuple<String, Double> tfIsf = new Tuple<>("k1", (Double)s.getFeature("tf-isf")); 
				Tuple<String, Double> locLen = new Tuple<>("loc_len", (Double)s.getFeature("loc-len")); 
				Tuple<String, Double> titleWords = new Tuple<>("k2", (Double)s.getFeature("title-words-relative")); 
				
				List<Tuple<String, Double>> inputVariables = new ArrayList<>(Arrays.asList(tfIsf, locLen, titleWords));
				Double out = fs.run(inputVariables, "informatividade");
		
			});
		});
	
		
		// gerar sumario
		
		// avaliar
		//Evaluation evaluation = new Evaluation();
		
		// otimizações
	
		
	}

}
