package summ.fuzzy.optimization.mutation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import summ.fuzzy.optimization.functions.BellFunction;

class GaussianMutationTest {

	private static final Logger log = LogManager.getLogger(GaussianMutationTest.class);
	private GaussianMutation m;
	
	public void testCoeffientGeneration(double minRange, double maxRange) {
//		double c = m.getAleatoryFeasibleCoefficient(minRange, maxRange);
//		log.info("Coefficient Range: [" + minRange + ", " + maxRange + "] Generated: " + c);
//		assertTrue(c >= minRange);
//		assertTrue(c <= maxRange);
	}
	
	@Test
	void test() {
		this.m = new GaussianMutation(new BellFunction());
		this.testCoeffientGeneration(0.0, 1.0);
		this.testCoeffientGeneration(0.1, 3.0);
		this.testCoeffientGeneration(0.5, 0.9);
		this.testCoeffientGeneration(0.9, 1.0);
		this.testCoeffientGeneration(1.0, 1.0);
		this.testCoeffientGeneration(0.0, 0.5);
	}

}
