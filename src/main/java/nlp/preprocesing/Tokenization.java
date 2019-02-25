package nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Text;
import model.Word;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokenization {

	public static Text tokenizeTextSentences(Text text) {
		InputStream model = null;
		try {
			model = new FileInputStream("resources/models/pt-token.bin");
			TokenizerModel sm = new TokenizerModel(model);
			// uses maximum entropy model 
			TokenizerME tk = new TokenizerME(sm);
			
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					ArrayList<Word> words = new ArrayList<Word>();
					for (String s : tk.tokenize(sentence.getEditedSentence())) {
						words.add(new Word(s));
					}
					sentence.setWords(words);
				});
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(model != null) {
				try {
					model.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return text;
	}
	
	
}
