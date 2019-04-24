package summ.fuzzy.optimization.mutation;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

class UniformMutationTest {

	private static final Logger log = LogManager.getLogger(UniformMutationTest.class);
	private Mutation m;
	
	public void testCoeffientGeneration(double minRange, double maxRange) {
		double c = m.getAleatoryFeasibleCoefficient(minRange, maxRange);
		log.info("Coefficient Range: [" + minRange + ", " + maxRange + "] Generated: " + c);
		assertTrue("Error, must be more or equals " + minRange, c >= minRange);
		assertTrue("Error, must be less or equals " + maxRange, c <= maxRange);
	}
	
	@Test
	void test() {
		this.m = new UniformMutation();
		this.testCoeffientGeneration(0.0, 1.0);
		this.testCoeffientGeneration(0.1, 3.0);
		this.testCoeffientGeneration(0.5, 0.9);
		this.testCoeffientGeneration(0.9, 1.0);
		this.testCoeffientGeneration(1.0, 1.0);
		this.testCoeffientGeneration(0.0, 0.5);
	}

}
