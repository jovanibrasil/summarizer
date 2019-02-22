package nlp.preprocesing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Stream;

import model.Text;
import model.Word;

public class StopWords {

	private static HashSet<String> stopWords = new HashSet<String>();
	//static Logger logger = Logger.getLogger(StopWordsHandler.class);

	public StopWords(String stopFile) {
		loadStopWords(stopFile);
	}

	public static boolean isStopWord(String str) {
		if (stopWords.contains(str))
			return true;
		return false;
	}
	
	public static Text removeStopWords(Text text) {
		try {
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					Map<String, Word> words = new HashMap<>();
					sentence.getWords().values().forEach(word -> {
						if(!StopWords.isStopWord(word.getRawWord())){
							words.put(word.getRawWord(), new Word(word.getRawWord()));
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

	private static void loadStopWords(String stopFile) {

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

}