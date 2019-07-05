package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.functions.Function;
import summ.fuzzy.optimization.mutation.MutationOperatorFactory.MutationOperatorType;

public class CreepMutation extends MutationOperator {
	
	private MutationOperatorType type;
	private double max;
	private double min; 
	
	public CreepMutation(MutationOperatorType creepType, Function function) {
		super();
		this.type = creepType;
		
		if(this.type.equals(MutationOperatorType.NORMAL_CREEP)) {
			this.max = +0.05; this.min = -0.05;
		}else {
			this.max = 1.2; this.min = 0.98; 
		}
	}

	/**
	 * Add a random number to the gene current value. This random number can be obtained
	 * from a normal (mean zero and low standard deviation) or a uniform distribution.  
	 */
	public double creepNormal(double val, double rangeMin, double rangeMax) {
		double result, disturb;
		int counter = 0;
		while(counter < 100) {
			disturb = this.min + (this.max - this.min) * this.rand.nextDouble(); 
			result = val + disturb;
			if(result >= rangeMin && result <= rangeMax) return result;
			counter++;
		}
		return val;
	}
	
	/**
	 * Multiply the gene value by a value close to one. This will cause a disturb in
	 * the chromossome value that can lead to a converge point. 
	 **/
	public double creepDisturb(double val, double rangeMin, double rangeMax) {
		double result, disturb;
		int counter = 0;
		while(counter < 100) { // try to find a feasible value 100 times 
			disturb = this.min + (this.max - this.min) * this.rand.nextDouble(); 
			result = val * disturb;
			if(result >= rangeMin && result <= rangeMax) return result;
			counter++;
		}
		return val;
	}
	
	@Override
	public String toString() {
		return "Creep mutation";
	}

	@Override
	public double getAleatoryFeasibleCoefficient(int index) {
		return 0;
	}

	@Override
	public double getAleatoryFeasibleCoefficient(double min, double max) {
		if(this.type.equals(MutationOperatorType.NORMAL_CREEP)) {
			return this.creepNormal(this.value, min, max);
		}else {
			return this.creepDisturb(this.value, min, max);
		}
	}	
	
}
