package summ.nlp.preprocesing;

import java.io.IOException;

import org.cogroo.dictionary.LemmaDictionary;
import org.cogroo.dictionary.impl.FSADictionary;

import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class Lemmatizer implements Pipe<Text> {

	private LemmaDictionary dict;

	public Lemmatizer(LemmaDictionary dict) { 
	    this.dict = dict; 
	}

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
			//dict = FSADictionary.createFromResources("/resources/dictionaries/jspell/ptbr.dic");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lemmatize(text);
	}

}
