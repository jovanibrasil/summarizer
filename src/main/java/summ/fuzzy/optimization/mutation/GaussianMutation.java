package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.functions.Function;

public class GaussianMutation extends MutationOperator {

	private Function function;
	
	public GaussianMutation(Function function) {
		super();
		this.function = function;
	}
	
	/**
	 * Substitui um gene por um valor aleatorio de uma distribuicao normal, dentro dos limites do 
	 * intervalo permitido para cada gene.
	 * 
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
				return rangeMin + (rangeMax - rangeMin) / 2;
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
		// TODO Auto-generated method stub
		return 0;
	}
	
}
