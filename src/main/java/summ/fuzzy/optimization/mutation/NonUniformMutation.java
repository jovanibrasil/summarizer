package summ.fuzzy.optimization.mutation;

import java.util.Random;

import summ.fuzzy.optimization.functions.Function;

/**
 * @author Jovani Brasil.
 * 
 * Gaussian mutation: the new value of a gene is a random number from an non uniform
 * distribution (Michalewicz, 1994).
 * 
 */
public class NonUniformMutation extends MutationOperator {
	
	private Random rand;
	private Function function;
	
	public NonUniformMutation(Function function) {
		this.rand = new Random(); 
		this.function = function;
	}
	
	/**
	 * Returns a random gene from a non-uniform distribution.
	 *
	 * @param g is the current generation
	 * @param gmax is the max number of generations
	 * @param e is a system parameter 
	 * @param geneValue is the gene value
	 * @param a ? min value? 
	 * @param b ? max value?
	 * 
	 */
	public double nonUniform(int g, int gmax, double geneValue, double e, double a, double b) {
		
		double f = Math.pow(rand.nextDouble() * (1 - ((double)g / gmax)), e);
		
		if(rand.nextFloat() < 0.5) {
			return geneValue + (b - geneValue) * f;
		} else {
			return geneValue - (geneValue - a) * f;
		}
	}
	
	@Override
	public double getAleatoryFeasibleCoefficient(int index) {
		return nonUniform(0, 5, 5, 0.5, this.function.getRangeMin(index), 
				this.function.getRangeMax(index));
	}	
	
	@Override
	public String toString() {
		return "Non-uniform mutation";
	}

	@Override
	public double getAleatoryFeasibleCoefficient(double min, double max) {
		return 0;
	}
	
}
