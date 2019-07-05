package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.functions.Function;

/**
 * @author Jovani Brasil.
 * 
 * Gaussian mutation: the new value of a gene is a random number from an normal distribution.
 * 
 */
public class GaussianMutation extends MutationOperator {

	private Function function;
	
	public GaussianMutation(Function function) {
		super();
		this.function = function;
	}
	
	/**
	 * Returns a random value from a normal distribution with specified mean
	 * and standard deviation.
	 * 
	 * @param mean
	 * @param standardDeviation
	 * @return
	 */
	public double gaussian(double mean, double standardDeviation) {
		return this.rand.nextGaussian() * standardDeviation + mean; 
	}

	@Override
	public double getAleatoryFeasibleCoefficient(int index) {
		double rangeMin = this.function.getRangeMin(index);
		double rangeMax = this.function.getRangeMax(index);
		if (rangeMax == rangeMin) return rangeMax;
		double mean = 0.0;
		double standardDeviation = 1.0;
		// normally distributed double with mean 0.0 and standard deviation 1.0
		double val = rangeMin - 1;
		int counter = 0;
		while(val < rangeMin || val > rangeMax){
			val = gaussian(mean, standardDeviation);
			counter++;
			if(counter == 40) {
				return rangeMin + (rangeMax - rangeMin) / 2; // mean between the range
			}
		}
		return val; 
	}
	
	@Override
	public String toString() {
		return "Gaussian mutation";
	}

	@Override
	public double getAleatoryFeasibleCoefficient(double min, double max) {
		return 0;
	}
	
}
