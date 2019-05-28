package summ.fuzzy.optimization.mutation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import summ.fuzzy.optimization.functions.BellFunction;
import summ.fuzzy.optimization.mutation.MutationOperatorFactory.MutationOperatorType;

class CreepMutationTest {

	private static final Logger log = LogManager.getLogger(CreepMutationTest.class);
	private CreepMutation m;
	
	public void testCoeffientGeneration(double val, double minRange, double maxRange) {
		double c = m.creepNormal(val, minRange, maxRange);
		log.info("Coefficient Value: " + val + " Range: [" + minRange + ", " + maxRange + "] Generated: " + c);
		assertTrue(c >= minRange);
		assertTrue(c <= maxRange);
	}
	
	@Test
	void testNormalCreepMutation() {
		this.m = new CreepMutation(MutationOperatorType.NORMAL_CREEP, new BellFunction());
		this.testCoeffientGeneration(0.5, 0.0, 1.0);
		this.testCoeffientGeneration(2.0, 0.1, 3.0);
		this.testCoeffientGeneration(0.7, 0.5, 0.9);
		this.testCoeffientGeneration(0.95, 0.9, 1.0);
		this.testCoeffientGeneration(1.0, 1.0, 1.0);
		this.testCoeffientGeneration(0.3, 0.0, 0.5);
	}
	
	@Test
	void testDisturbCreepMutation() {
		this.m = new CreepMutation(MutationOperatorType.DISTURB_CREEP, new BellFunction());
		this.testCoeffientGeneration(0.5, 0.0, 1.0);
		this.testCoeffientGeneration(2.0, 0.1, 3.0);
		this.testCoeffientGeneration(0.7, 0.5, 0.9);
		this.testCoeffientGeneration(0.95, 0.9, 1.0);
		this.testCoeffientGeneration(1.0, 1.0, 1.0);
		this.testCoeffientGeneration(0.3, 0.0, 0.5);
	}

}
