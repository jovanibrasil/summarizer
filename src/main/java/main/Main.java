package main;

import features.Features;
import model.Text;
import nlp.preprocesing.Preprocessing;
import utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		//Evaluation evaluation = new Evaluation();
		//FuzzySystem fs = new FuzzySystem();
		
		// Stream of lines, that is equivalent to paragraphs.
		Text text = Utils.readFile("ce94ab10-a.txt");
		
		
		if(text != null) {
			Preprocessing pp = new Preprocessing(text);
			
		}
		
		// V palavras devem ser armazenadas também em um vetor, para possibilitar contagem e analise de ordem 
		// eliminar linhas vazias
		// eliminar espaços antes e depois dos termos
		// computar features
		Features f = new Features(text);
		
		// integrar com o sistema fuzzy
		
		// gerar sumario
		
		// avaliar
		
		// otimizações
		
	}

}
