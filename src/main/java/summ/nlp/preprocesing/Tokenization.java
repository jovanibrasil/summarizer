package summ.nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class Tokenization implements Pipe<Text> {

	private PreProcessingTypes tokenizationType;
	
	public Tokenization(PreProcessingTypes tokenizationType) {
		this.tokenizationType = tokenizationType;
	}
	
	/**
	 * 
	 * Considera que não existem palavras tokenizadas, ou seja, lista de palavras é vazia.
	 * 
	 */
	public Text tokenization(Text text) {
		InputStream model = null;
		try {
			model = new FileInputStream("resources/models/pt-token.bin");
			TokenizerModel sm = new TokenizerModel(model);
			// uses maximum entropy model 
			TokenizerME tk = new TokenizerME(sm);
			
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					String tokenizedWords[] = tk.tokenize(sentence.getEditedSentence());
					for (String s : tokenizedWords) {
						sentence.addWord(new Word(s));
					}
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
	
	/**
	 * Sentence tokenization based only on whitespace characters as delimiters. 
	 */
	public Text whiteSpaceTokenization(Text text) {
		
		WhitespaceTokenizer tk = WhitespaceTokenizer.INSTANCE;
		
		text.getParagraphs().forEach( paragraph -> {
			paragraph.getSentences().forEach(sentence -> {
				for (String s : tk.tokenize(sentence.getEditedSentence())) {
					sentence.addWord(new Word(s));
				}
			});
		});
		
		return text;
	}
	
	/**
	 * Sentence tokenization that splits sentence into numbers, words and punctuation.
	 */
	public  Text simpleTokenization(Text text) {
		
		SimpleTokenizer tk = SimpleTokenizer.INSTANCE;
		
		text.getParagraphs().forEach( paragraph -> {
			paragraph.getSentences().forEach(sentence -> {
				for (String s : tk.tokenize(sentence.getEditedSentence())) {
					sentence.addWord(new Word(s));
				}
			});
		});
		
		return text;
	}

	@Override
	public Text process(Text text) {
		switch (this.tokenizationType) {
			case WHITE_SPACE_TOKENIZATION:
				return this.whiteSpaceTokenization(text);
			case SIMPLE_TOKENIZATION:
				return this.simpleTokenization(text);
			case NEURAL_TOKENIZATION:
				return this.tokenization(text);
		}
		return text;
	}
	
}
