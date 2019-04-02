package summ.nlp.preprocesing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class StopWords implements Pipe<Text> {

	private HashSet<String> stopWords = null;
	//static Logger logger = Logger.getLogger(StopWordsHandler.class);

	public boolean isStopWord(String str) {
		if (this.stopWords.contains(str))
			return true;
		return false;
	}
	
	public Text removeStopWords(Text text) {
		
		if(this.stopWords == null) {
			loadStopWords("resources/stopwords-pt-br.txt");
		}
		try {
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					ArrayList<Word> words = new ArrayList<Word>();
					sentence.getWords().forEach(word -> {
						if(!this.isStopWord(word.getRawWord())){
							words.add(new Word(word.getRawWord()));
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

	private void loadStopWords(String stopFile) {

		stopWords = new HashSet<String>();
		try (Stream<String> stream = Files.lines(Paths.get(stopFile))) {
			stream.forEach(line -> {
				String stopWord = line.trim().toLowerCase();
				stopWords.add(stopWord);
			});

		} catch (IOException e) {
//			logger.warn("Problem with stopwords file: " + e.getMessage());
//			logger.warn("If you want to use stop words, please fix this issue first before proceeding.");
		}
	}

	@Override
	public Text process(Text text) {
		return this.removeStopWords(text);
	}

}