package summ.nlp.features;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import summ.model.Text;
import summ.nlp.preprocesing.Lemmatizer;
import summ.nlp.preprocesing.Misc;
import summ.nlp.preprocesing.POSTagger;
import summ.nlp.preprocesing.PreProcessingTypes;
import summ.nlp.preprocesing.SentenceSegmentation;
import summ.nlp.preprocesing.StopWords;
import summ.nlp.preprocesing.Titles;
import summ.nlp.preprocesing.Tokenization;
import summ.utils.FileUtils;
import summ.utils.Pipeline;

public class TextRankTest {

	public static Text text;
	public static TextRank textRank;
	
	public static Pipeline<Text> getTextPreProcessingPipeline() {
		return new Pipeline<Text>(new SentenceSegmentation(), new Misc(PreProcessingTypes.TO_LOWER_CASE),
				new Misc(PreProcessingTypes.REMOVE_PUNCTUATION), new Tokenization(PreProcessingTypes.ME_TOKENIZATION),
				new Titles(), new StopWords(), new POSTagger(), new Lemmatizer());
	}
	
	public static Pipeline<Text> getFeaturePipeline() {
		return new Pipeline<Text>(
				new Location(), new Length(), new LocLen(), new Frequency(), 
					new Title(), new TextRank(FeatureType.TFISF));
	}
	
	@BeforeAll
    protected static void init(){
		text = FileUtils.loadText("./corpora/temario-2004/full-texts/ce94ab10-a.txt");
		text = getTextPreProcessingPipeline().process(text);
		text = getFeaturePipeline().process(text);
		textRank = new TextRank(FeatureType.TFISF);
	}
	
	@Test
	void testCalculateSimilarityMatrix() {
		Assertions.assertDoesNotThrow(() -> {
			textRank.calculateSimilarityMatrix(text);
		});
	}
	
	@Test
	void testCalculateSentenceRank() {
		Assertions.assertDoesNotThrow(() -> {
			double[][] similarityMatrix = textRank.calculateSimilarityMatrix(text);
			textRank.calculateSentenceRank(text, similarityMatrix);
		});
	}
	
	@Test
	void textCalculateTextRank() {
		Assertions.assertDoesNotThrow(() -> {
			textRank.calculateTextRank(text, null);
		});
	}
	
}
