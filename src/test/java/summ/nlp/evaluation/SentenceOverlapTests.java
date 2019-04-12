package summ.nlp.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class SentenceOverlapTests {

	private static final double DELTA = 1e-6;
	
	@Test
	void evaluateEmptyTexts() {
		this.evaluate(0, 0, 0, 0, 0, 0);
	}
	@Test
	void evaluateEmptyGeneratedText() {
		this.evaluate(12, 0, 0, 0, 0, 0);
	}
	@Test
	void evaluateEmptyReferenceText() {
		this.evaluate(0, 12, 0, 0, 0, 0);
	}
	@Test
	void evaluateDifferentTexts() {
		this.evaluate(12, 12, 0, 0, 0, 0);
	}
	@Test
	void evaluateEqualTexts() {
		this.evaluate(12, 12, 12, 1, 1, 1);
	}
	@Test
	void evaluateValidTexts1() {
		this.evaluate(12, 10, 3, 0.3, 0.25, 0.272727273);
	}
	@Test
	void evaluateValidTexts2() {
		this.evaluate(12, 14, 6, 0.428571429, 0.5, 0.461538462);
	}
	@Test
	void evaluateValidTexts3() {
		this.evaluate(12, 14, 12, 0.8571428571, 1.0, 0.9230769231);
	}
	
	public void evaluate(double relevantSentences, double retrievedSentences,  double correctSentences,
			double expectedPrecision, double expectedRecall, double expectedfMeasure) {
		HashMap<String, Double> result = SentenceOverlap.calculateMetrics(relevantSentences, retrievedSentences, correctSentences);
		assertEquals(expectedPrecision, (double)result.get("precision"), DELTA);
		assertEquals(expectedRecall, (double)result.get("recall"), DELTA);
		assertEquals(expectedfMeasure, (double)result.get("fMeasure"), DELTA);
	}
}
