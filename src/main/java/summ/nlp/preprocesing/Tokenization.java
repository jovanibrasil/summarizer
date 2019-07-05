package summ.nlp.preprocesing;

import java.io.InputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import summ.model.Sentence;
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
	 * Maximum entropy based sentence tokenization. 
	 * 
	 * @param text
	 * 
	 */
	public Text tokenization(Text text) {
		InputStream model = null;
		try {
			log.debug("Executing model based tokenization for each sentence in the text " + text.getName());
			model = ClassLoader.getSystemClassLoader().getResourceAsStream("models/pt-token.bin");
			// TokenizerModel encapsulates the model and provides basic methods
			TokenizerModel sm = new TokenizerModel(model); // This model was trained on conllx bosque data.
			// Uses maximum entropy with the defined model 
			TokenizerME tk = new TokenizerME(sm);
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					String tokenizedWords[] = tk.tokenize(sentence.getCurrentValue());
					for (String s : tokenizedWords) {
						//String string = Normalizer.normalize(s.trim(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
						text.wordSet.add(s.trim());
						sentence.wordList.add(s.trim());
						sentence.addWord(new Word(s.trim()));
					}
					sentence.wordCounter = tokenizedWords.length;
					text.wordCounter += tokenizedWords.length;
				});
			});
			
		} catch (Exception e) {
			log.warn("Problem with tokenization model file. " + e.getMessage());
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
	 * 
	 * @param text
	 * 
	 */
	public Text whiteSpaceTokenization(Text text) {
		log.debug("Executing whitespace based tokenization for each sentence in the text " + text.getName());
		WhitespaceTokenizer tk = WhitespaceTokenizer.INSTANCE;
		text.getParagraphs().forEach( paragraph -> {
			paragraph.getSentences().forEach(sentence -> {
				String[] tokenizedWords = tk.tokenize(sentence.getCurrentValue());
				for (String s : tokenizedWords) {
					sentence.addWord(new Word(s.trim()));
				}
				sentence.wordCounter = tokenizedWords.length;
				text.wordCounter += tokenizedWords.length;
			});
		});
		return text;
	}
	
	/**
	 * Sentence tokenization that splits sentence into numbers, words and punctuation.
	 * 
	 * @param text
	 * 
	 */
	public  Text simpleTokenization(Text text) {
		log.debug("Executing simple tokenization for each sentence in the text " + text.getName());
		SimpleTokenizer tk = SimpleTokenizer.INSTANCE;
		text.getParagraphs().forEach( paragraph -> {
			paragraph.getSentences().forEach(sentence -> {
				String[] tokenizedWords = tk.tokenize(sentence.getCurrentValue());
				for (String s : tokenizedWords) {
					sentence.addWord(new Word(s.trim()));
				}
				sentence.wordCounter = tokenizedWords.length;
				text.wordCounter += tokenizedWords.length;
			});
		});
		return text;
	}

	@Override
	public String toString() {
		return "Tokenization";
	}
	
	@Override
	public Text process(Text text) {
		
		//The sentence list of words must be empty before the execution.
		for (Sentence sentence : text.getSentences()) { 
			if(sentence.getLength() > 0) { 
				log.error("Sentence " + sentence.getId() + ": List of words must be empty."); 
			} 
		}
		switch (this.tokenizationType) {
			case WHITE_SPACE_TOKENIZATION:
				return this.whiteSpaceTokenization(text);
			case SIMPLE_TOKENIZATION:
				return this.simpleTokenization(text);
			case ME_TOKENIZATION:
				return this.tokenization(text);
			default:
				log.warn("Tokenization method not found.");
				break;
		}
		return text;
	}
	
}
