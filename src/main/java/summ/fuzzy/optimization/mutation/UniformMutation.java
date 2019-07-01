package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.functions.Function;

/**
 * @author Jovani Brasil.
 *
 * Uniform mutation: the new value of a gene is a random number from an uniform distribution 
 * between the specified limits.
 *
 */
public class UniformMutation extends MutationOperator {

	private Function function;
	
	public UniformMutation(Function function) {
		super();
		this.function = function;
	}
	
	/**
	 * Returns a random value from an uniform distribution between rangeMin and rangeMax.
	 * 
	 * @param rangeMin
	 * @param rangeMax
	 * @return
	 */
	public double uniform(double rangeMin, double rangeMax) {
		return rangeMin + (rangeMax - rangeMin) * this.rand.nextDouble(); // uniformly distributed double
	}

	@Override
	public double getAleatoryFeasibleCoefficient(int index) {
		return this.uniform(function.getRangeMin(index), function.getRangeMax(index));
	}
	
	@Override
	public String toString() {
		return "Uniform mutation";
	}

	@Override
	public double getAleatoryFeasibleCoefficient(double min, double max) {
		return this.uniform(min, max);
	}
	
}
