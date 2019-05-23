package summ.fuzzy.optimization.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;

public class ErrorFunctionSummarizationTest {

	@Test
	void testMin() {
		ErrorFunctionSummarization ef = new ErrorFunctionSummarization(null, null, null, null);
		assertEquals(0.5, ef.min(0.8, 0.5));
		assertEquals(0.5, ef.min(0.5, 0.8));
	}

	@Test
	void testMax() {
		ErrorFunctionSummarization ef = new ErrorFunctionSummarization(null, null, null, null);
		assertEquals(0.8, ef.max(0.8, 0.5));
		assertEquals(0.8, ef.max(0.5, 0.8));
	}

	@Test
	void testOverlap() {
		ErrorFunctionSummarization ef = new ErrorFunctionSummarization(null, null, null, null);
		assertEquals(0, ef.overlap(0.3, 0.1, 0.6, 0.1), 0.00001);
		assertEquals(0.3, ef.overlap(0.3, 0.3, 0.6, 0.3), 0.00001);
		assertEquals(0.2, ef.overlap(0.3, 0.3, 0.6, 0.2), 0.00001);
		assertEquals(0.2, ef.overlap(0.3, 0.2, 0.6, 0.3), 0.00001);
	}

	@Test
	void testRange() {
		ErrorFunctionSummarization ef = new ErrorFunctionSummarization(null, null, null, null);
		RealVector chromossome = new ArrayRealVector(		
			new double[] { 0.2, 0.0, 0.3, 0.2, 0.2, 0.6, 0.2, 0.0, 0.9,
				0.3, 0.0, 0.3, 0.2, 0.2, 0.6, 0.2, 0.0, 0.9,
				0.4, 0.0, 0.3, 0.2, 0.2, 0.6, 0.4, 0.0, 0.9}
		);
		assertEquals(1.2, ef.range(chromossome, 0), 0.00001);
		assertEquals(1.4, ef.range(chromossome, 9), 0.00001);
		assertEquals(2.0, ef.range(chromossome, 18), 0.00001);
	}

	@Test
	void testCoverage_factor() {
		ErrorFunctionSummarization ef = new ErrorFunctionSummarization(null, null, null, null);
		RealVector chromossome = new ArrayRealVector(		
				new double[] { 0.2, 0.0, 0.3, 0.2, 0.2, 0.6, 0.2, 0.0, 0.9,
						0.3, 0.0, 0.3, 0.2, 0.2, 0.6, 0.2, 0.0, 0.9,
						0.4, 0.0, 0.3, 0.2, 0.2, 0.6, 0.4, 0.0, 0.9 }
		);
		assertEquals(0.833333333, ef.coverage_factor(chromossome, 0), 0.00000001);
	}
	
	@Test
	void testOverlap_factor() {
		ErrorFunctionSummarization ef = new ErrorFunctionSummarization(null, null, null, null);
		RealVector chromossome = new ArrayRealVector(		
				new double[] { 0.2, 0.0, 0.3, 0.2, 0.2, 0.6, 0.2, 0.0, 0.9,
						0.3, 0.0, 0.3, 0.2, 0.2, 0.6, 0.2, 0.0, 0.9,
						0.4, 0.0, 0.3, 0.2, 0.2, 0.6, 0.4, 0.0, 0.9 }
		);
		assertEquals(0.0, ef.overlap_factor(chromossome, 0), 0.00001);
	}
	
}
