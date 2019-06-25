package summ.nlp.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SentenceOverlapTest {

	private static final double DELTA = 1e-6;
	private static SentenceOverlap so;
	
	@BeforeAll
    protected static void init(){
		so = new SentenceOverlap(EvaluationTypes.ALL);
	}
	
	@Test
	void testCalculateMetricsEmptyTexts() {
		this.calculateMetrics(0, 0, 0, 0, 0, 0);
	}
	@Test
	void testCalculateMetricsEmptyGeneratedText() {
		this.calculateMetrics(12, 0, 0, 0, 0, 0);
	}
	@Test
	void testCalculateMetricsEmptyReferenceText() {
		this.calculateMetrics(0, 12, 0, 0, 0, 0);
	}
	@Test
	void testCalculateMetricsDifferentTexts() {
		this.calculateMetrics(12, 12, 0, 0, 0, 0);
	}
	@Test
	void testCalculateMetricsEqualTexts() {
		this.calculateMetrics(12, 12, 12, 1, 1, 1);
	}
	@Test
	void testCalculateMetricsValidTexts1() {
		this.calculateMetrics(12, 10, 3, 0.3, 0.25, 0.272727273);
	}
	@Test
	void testCalculateMetricsValidTexts2() {
		this.calculateMetrics(12, 14, 6, 0.428571429, 0.5, 0.461538462);
	}
	@Test
	void testCalculateMetricsValidTexts3() {
		this.calculateMetrics(12, 14, 12, 0.8571428571, 1.0, 0.9230769231);
	}
	@Test
	void testCalculateMetricsValidTexts4() {
		this.calculateMetrics(13, 14, 7, 0.5, 0.5384615385, 0.5185185185);
	}
	
	public void calculateMetrics(double relevantSentences, double retrievedSentences,  double correctSentences,
			double expectedPrecision, double expectedRecall, double expectedfMeasure) {
		EvaluationResult result = so.calculateMetrics(relevantSentences, retrievedSentences, correctSentences);
		assertEquals(expectedPrecision, result.getMetricValue(EvaluationTypes.PRECISION.name()), DELTA);
		assertEquals(expectedRecall, result.getMetricValue(EvaluationTypes.RECALL.name()), DELTA);
		assertEquals(expectedfMeasure, result.getMetricValue(EvaluationTypes.FMEASURE.name()), DELTA);
	}
}
