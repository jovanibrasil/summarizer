package summ.nlp.features;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;

public class FrequencyTests {

	private static Paragraph p1;
	private static Text t;
	private static final double DELTA = 1e-6;
	
	public static List<Word> getListOfWordObjects(List<String> words){
		return words.stream().map(w -> {
			return new Word(w);
		}).collect(Collectors.toList());
	}
	
	@BeforeEach
    protected void init(){
		t = new Text("Isso � apenas um teste.");
		
		Paragraph p0 = new Paragraph("Test title ...");
		Sentence s0 = new Sentence("Test sentence ...");
		s0.setId(0);
		s0.setWords(getListOfWordObjects(Arrays.asList("�", "tomara", "que", "hoje", "eu", "acabe", "meu", "tcc")));
		p0.addSentence(s0);
		t.addParagraph(p0);
		p1 = new Paragraph("Paragraph 1");
		t.addParagraph(p1);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testTF() {
		
		Sentence s1 = new Sentence("Senten�a 1");
		s1.setId(1);
		s1.setWords(getListOfWordObjects(Arrays.asList("meu", "trabalho", "�", "legal", "legal")));
		Sentence s2 = new Sentence("Senten�a 2");
		s2.setId(2);
		s2.setWords(getListOfWordObjects(Arrays.asList("�", "tomara", "que", "eu", "chegue"))); // "ao" "resultado"
		p1.addSentence(s1);
		p1.addSentence(s2);
		
		Frequency f = new Frequency();
		f.tf(t);
		f.isf(t);
		Map<String, Integer> tf = (Map<String, Integer>) t.getFeature("rtf");
		
		assertEquals(1, (int)tf.get("tcc")); // Termo com uma ocorr�ncia
		assertEquals(2, (int)tf.get("legal")); // Termo com duas ocorr�ncias
		assertEquals(3, (int)tf.get("�"));
		assertEquals(2, (int)tf.get("tomara"));
		assertEquals(1, (int)tf.get("trabalho"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void testISF() {
		
		Sentence s1 = new Sentence("Senten�a 1");
		s1.setId(1);
		s1.setWords(getListOfWordObjects(Arrays.asList("meu", "trabalho", "�", "um", "trabalho", "legal")));
		Sentence s2 = new Sentence("Senten�a 2");
		s2.setId(2);
		s2.setWords(getListOfWordObjects(Arrays.asList("�", "tomara", "que", "eu", "chegue"))); // "ao" "resultado"
		p1.addSentence(s1);
		p1.addSentence(s2);
		
		Frequency f = new Frequency();
		f.tf(t);
		f.isf(t);
		Map<String, Double> isf = (Map<String, Double>) t.getFeature("isf");
		// Termo com uma ocorr�ncia isf=0.03010299956639812
		assertEquals(0.4771212547, isf.get("tcc"), DELTA); 
		// Termo com duas ocorr�ncias na mesma senten�a isf=0.06020599913279624
		assertEquals(0.1760912590, isf.get("tomara"), DELTA);
		// Ocorr�ncia em ambas as senten�as isf=0
		assertEquals(0, isf.get("�"), DELTA); 
	}
	
	@Test
	void testTFISF() {
	
		Sentence s1 = new Sentence("Senten�a 1");
		s1.setWords(getListOfWordObjects(Arrays.asList("meu", "trabalho", "�", "legal", "legal")));
		s1.setId(1);
		p1.addSentence(s1);
		Frequency f = new Frequency();
		f.tf(t);
		f.isf(t);
		f.tfIsf(t);
		// S� com s1 todos  os tf-isf s�o 0, uma vez que isf = 0
		// 0.4771212547 * 2 = 0.9542425094
		assertEquals(0.5000000, (double)t.getSentenceById(0).getWord("tcc").getFeature("tf_isf"), DELTA); 
	}
	
	@Test
	void test3() {
		Sentence s1 = new Sentence("Senten�a 1");
		s1.setWords(getListOfWordObjects(Arrays.asList("meu", "tcc", "�", "legal", "legal")));
		Sentence s2 = new Sentence("Senten�a 2");
		s2.setWords(getListOfWordObjects(Arrays.asList("�", "tomara", "que", "eu", "chegue"))); // "ao" "resultado"
		Sentence s3 = new Sentence("Senten�a 3");
		s3.setWords(getListOfWordObjects(Arrays.asList("hoje", "�", "sabado")));
		Sentence s4 = new Sentence("Senten�a 4");
		s4.setWords(getListOfWordObjects(Arrays.asList("e", "nem", "olhei", "netflix")));
		Sentence s5 = new Sentence("Senten�a 5");
		s5.setWords(getListOfWordObjects(Arrays.asList("nao", "jantei", "nadinha", "hoje")));
		Sentence s6 = new Sentence("Senten�a 6");
		s6.setWords(getListOfWordObjects(Arrays.asList("minha", "namorada", "�", "t�o", "braba", "comigo", "comigo")));
		p1.addSentence(s1);
		p1.addSentence(s2);
		p1.addSentence(s3);
		p1.addSentence(s4);
		p1.addSentence(s5);
		p1.addSentence(s6);
		
		// TODO ...	
	}
	
	// testar tfidf da senten�a
	
}

