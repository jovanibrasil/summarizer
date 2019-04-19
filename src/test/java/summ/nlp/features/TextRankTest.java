package summ.nlp.features;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import summ.model.Text;
import summ.nlp.features.FeatureType;
import summ.nlp.features.TextRank;
import summ.summarizer.Summarizer;

public class TextRankTest {

	public static Text text;
	public static TextRank textRank;
	
	@BeforeAll
    protected static void init(){
		text =  Summarizer.getTextProcessedText("projects/temario-2014/full-texts/", 
				"ce94ab10-a.txt");
		textRank = new TextRank(FeatureType.TFISF);
	}
	
	@Test
	void testCalculateSimilarityMatrix() {
		Assertions.assertDoesNotThrow(() -> {
			textRank.calculateSimilarityMatrix(this.text);
		});
	}
	
	@Test
	void testCalculateSentenceRank() {
		Assertions.assertDoesNotThrow(() -> {
			double[][] similarityMatrix = textRank.calculateSimilarityMatrix(this.text);
			textRank.calculateSentenceRank(text, similarityMatrix);
		});
	}
	
	@Test
	void textCalculateTextRank() {
		Assertions.assertDoesNotThrow(() -> {
			textRank.calculateTextRank(this.text, null);
		});
	}
	
}
