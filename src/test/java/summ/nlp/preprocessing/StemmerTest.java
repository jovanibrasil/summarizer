package summ.nlp.preprocessing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import summ.nlp.preprocesing.Stemmer;

public class StemmerTest {

	@Test
	void testStemWord() {
		Stemmer s = new Stemmer();
		assertEquals("trabalh", s.stemToken("trabalhando"));
	}
	
}
