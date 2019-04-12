package summ.nlp.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SentenceOverlapTests {

	private static final double DELTA = 1e-6;
	private static SentenceOverlap so;
	
	@BeforeAll
    protected static void init(){
		so = new SentenceOverlap();
	}
	
	@Test
	void calculateMetricsEmptyTexts() {
		this.calculateMetrics(0, 0, 0, 0, 0, 0);
	}
	@Test
	void calculateMetricsEmptyGeneratedText() {
		this.calculateMetrics(12, 0, 0, 0, 0, 0);
	}
	@Test
	void calculateMetricsEmptyReferenceText() {
		this.calculateMetrics(0, 12, 0, 0, 0, 0);
	}
	@Test
	void calculateMetricsDifferentTexts() {
		this.calculateMetrics(12, 12, 0, 0, 0, 0);
	}
	@Test
	void calculateMetricsEqualTexts() {
		this.calculateMetrics(12, 12, 12, 1, 1, 1);
	}
	@Test
	void calculateMetricsValidTexts1() {
		this.calculateMetrics(12, 10, 3, 0.3, 0.25, 0.272727273);
	}
	@Test
	void calculateMetricsValidTexts2() {
		this.calculateMetrics(12, 14, 6, 0.428571429, 0.5, 0.461538462);
	}
	@Test
	void calculateMetricsValidTexts3() {
		this.calculateMetrics(12, 14, 12, 0.8571428571, 1.0, 0.9230769231);
	}
	@Test
	void calculateMetricsValidTexts4() {
		this.calculateMetrics(13, 14, 7, 0.5, 0.5384615385, 0.5185185185);
	}
	
	public void calculateMetrics(double relevantSentences, double retrievedSentences,  double correctSentences,
			double expectedPrecision, double expectedRecall, double expectedfMeasure) {
		EvaluationResult result = so.calculateMetrics(relevantSentences, retrievedSentences, correctSentences);
		assertEquals(expectedPrecision, result.getMetric("precision"), DELTA);
		assertEquals(expectedRecall, result.getMetric("recall"), DELTA);
		assertEquals(expectedfMeasure, result.getMetric("fMeasure"), DELTA);
	}
}
