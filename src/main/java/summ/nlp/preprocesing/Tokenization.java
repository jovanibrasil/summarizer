package summ.nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class Tokenization implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(Tokenization.class);
	private PreProcessingTypes tokenizationType;
	
	public Tokenization(PreProcessingTypes tokenizationType) {
		this.tokenizationType = tokenizationType;
	}
	
	/**
	 * 
	 * Considera que no existem palavras tokenizadas, ou seja, lista de palavras  vazia.
	 * 
	 */
	public Text tokenization(Text text) {
		InputStream model = null;
		try {
			log.info("Executing model based tokenization for each sentence in the text " + text.getName());
			model = new FileInputStream("src/main/resources/models/pt-token.bin");
			TokenizerModel sm = new TokenizerModel(model);
			// uses maximum entropy model 
			TokenizerME tk = new TokenizerME(sm);
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					String tokenizedWords[] = tk.tokenize(sentence.getCurrentValue());
					for (String s : tokenizedWords) {
						sentence.addWord(new Word(s));
					}
				});
			});
			
		} catch (Exception e) {
			log.warn("Problem with tokenization model file." + e.getMessage());
			log.warn("If you want to use tokenization, please fix this issue first before proceeding.");
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
		log.info("Executing whitespace based tokenization for each sentence in the text " + text.getName());
		WhitespaceTokenizer tk = WhitespaceTokenizer.INSTANCE;
		text.getParagraphs().forEach( paragraph -> {
			paragraph.getSentences().forEach(sentence -> {
				for (String s : tk.tokenize(sentence.getCurrentValue())) {
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
		log.info("Executing simple tokenization for each sentence in the text " + text.getName());
		SimpleTokenizer tk = SimpleTokenizer.INSTANCE;
		text.getParagraphs().forEach( paragraph -> {
			paragraph.getSentences().forEach(sentence -> {
				for (String s : tk.tokenize(sentence.getCurrentValue())) {
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
			default:
				log.warn("Tokenization method not found.");
				break;
		}
		return text;
	}
	
}
