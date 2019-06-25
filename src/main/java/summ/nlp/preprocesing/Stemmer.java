package summ.nlp.preprocesing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.tartarus.snowball.ext.PortugueseStemmer;

import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;


public class Stemmer implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(Stemmer.class);
	
	public String stemToken(String token) {
		PortugueseStemmer ps = new PortugueseStemmer();
		ps.setCurrent(token);
		
		if(!ps.stem()) {
			return token;
		}else {
			return ps.getCurrent();
		}
	}
	
	@Override
	public Text process(Text text) {
		log.debug("Stemming each word in the text.");
		for (Sentence sentence : text.getSentences()) {
			for (Word word : sentence.getWords()) {
				String stemmedToken = this.stemToken(word.getInitialValue());
				word.setCurrentValue(stemmedToken); // set processed (stemmed) token 
			}
		}
		return text;
	}
	
	@Override
	public String toString() {
		return "Stemmer";
	}
	
}
