package summ.nlp.preprocesing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class StopWords implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(StopWords.class);
	private static HashSet<String> stopWords = null;
	
	/**
	 * Remove all stop words from text.
	 * 
	 * @param text
	 * @return
	 */
	public Text removeStopWords(Text text) {
		log.debug("Remove stopwords in the text " + text.getName());
		if(stopWords == null) {
			loadStopWords("/stopwords-pt-br.txt");
		}
		try {
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					ArrayList<Word> words = new ArrayList<Word>();
					sentence.getWords().forEach(word -> {
						if(!this.isStopWord(word.getInitialValue())){
							words.add(word);
						}
					});
					sentence.setWords(words);
				});
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return text;
	}
	
	/**
	 * Verifies if a word is a stop word. 
	 * 
	 * @param word
	 * @return
	 */
	public boolean isStopWord(String word) {
		if (stopWords.contains(word))
			return true;
		return false;
	}

	/**
	 * Load stop words file from the specified directory.
	 * 
	 * @param stopFilePath
	 */
	private void loadStopWords(String stopFilePath) {
		log.debug("Loading stopwords file.");
		stopWords = new HashSet<String>();
		try (InputStream text = getClass().getResourceAsStream(stopFilePath)){
			BufferedReader br = new BufferedReader(new InputStreamReader(text));
			String line;
		    while ((line = br.readLine()) != null) {
		    	String stopWord = line.trim().toLowerCase();
				stopWords.add(stopWord);
		    }
		    br.close();
		} catch (IOException e) {
			log.warn("Problem with stopwords file: " + e.getMessage());
			log.warn("If you want to use stop words, please fix this issue first before proceeding.");
		}
	}

	@Override
	public String toString() {
		return "Stopwords";
	}
	
	@Override
	public Text process(Text text) {
		return this.removeStopWords(text);
	}

}