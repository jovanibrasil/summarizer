package summ.nlp.preprocesing;

import org.tartarus.snowball.ext.PortugueseStemmer;

import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;


public class Stemmer implements Pipe<Text> {

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
		
		for (Sentence sentence : text.getSentences()) {
			for (Word word : sentence.getWords()) {
				String stemmedToken = this.stemToken(word.getRawWord());
				word.setProcessedToken(stemmedToken); // set processed (stemmed) token 
			}
		}
		return text;
	}
	
}
