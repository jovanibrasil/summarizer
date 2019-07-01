package summ.fuzzy.optimization.mutation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import summ.fuzzy.optimization.functions.GbellFunction;

class UniformMutationTest {

	private static final Logger log = LogManager.getLogger(UniformMutationTest.class);
	private UniformMutation m;
	
	public void testCoeffientGeneration(double minRange, double maxRange) {
		double c = m.uniform(minRange, maxRange);
		log.info("Coefficient Range: [" + minRange + ", " + maxRange + "] Generated: " + c);
		assertTrue(c >= minRange);
		assertTrue(c <= maxRange);
	}
	
	@Test
	void test() {
		this.m = new UniformMutation(new GbellFunction());
		this.testCoeffientGeneration(0.0, 1.0);
		this.testCoeffientGeneration(0.1, 3.0);
		this.testCoeffientGeneration(0.5, 0.9);
		this.testCoeffientGeneration(0.9, 1.0);
		this.testCoeffientGeneration(1.0, 1.0);
		this.testCoeffientGeneration(0.0, 0.5);
	}

}
