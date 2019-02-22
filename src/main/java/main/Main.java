package main;

import java.util.stream.Stream;

import fuzzy.FuzzySystem;
import nlp.preprocesing.Preprocessing;
import utils.Utils;

public class Main {

	public static void main(String[] args) {
		
		//Evaluation evaluation = new Evaluation();
		//FuzzySystem fs = new FuzzySystem();
		
		// Stream of lines, that is equivalent to paragraphs.
		String text = Utils.readFile("ce94ab10-a.txt");
		
		Preprocessing pp = new Preprocessing(text);
		
		
		
		
	}

}
