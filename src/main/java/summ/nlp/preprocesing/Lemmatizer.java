package summ.nlp.preprocesing;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.cogroo.dictionary.LemmaDictionary;
import org.cogroo.dictionary.impl.FSADictionary;

import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class Lemmatizer implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(Lemmatizer.class);
	
	private LemmaDictionary dict;

	public void analyzeToken(Word token) {
		String tag = token.getPosTag();
		String word = token.getInitialValue();
		String[] lemmas = dict.getLemmas(word, tag);
		if (lemmas == null || lemmas.length == 0) {
			lemmas = dict.getLemmas(word.toLowerCase(), tag);
		}
		
		if(lemmas != null && lemmas.length != 0) {
			token.setCurrentValue(lemmas[0]);
		}
	}

	public Text lemmatize(Text text) {
		log.info("Lemmatizing each word in text " + text.getName());
		for (Sentence s : text.getSentences()) {
			for (Word w : s.getWords()) {
				this.analyzeToken(w);
			}	
		}
		return text;
	}

	@Override
	public Text process(Text text) {
		try {
			this.dict = FSADictionary
			          .createFromResources("/fsa_dictionaries/pos/pt_br_jspell.dict");
		} catch (Exception e) {
			log.warn("Problem with the lemmatizer." + e.getMessage());
			log.warn("If you want to use lemmatizer, please fix this issue first before proceeding.");
		}
		return lemmatize(text);
	}

}
