package summ.fuzzy.optimization.mutation;

public class GaussianMutation extends MutationOperator {

	public GaussianMutation() {
		super();
	}
	
	/**
	 * Substitui um gene por um valor aleatorio de uma distribuicao normal, dentro dos limites do 
	 * intervalo permitido para cada gene.
	 * 
	 */
	public double gaussian(double rangeMin, double rangeMax) {
		double mean = 0.0;
		double standardDeviation = 1.0;
		// normally distributed double with mean 0.0 and standard deviation 1.0
		return rangeMin + (rangeMax - rangeMin) * (this.rand.nextDouble() * standardDeviation + mean); 
	}

	@Override
	public double getAleatoryFeasibleCoefficient(double rangeMin, double rangeMax) {
		return gaussian(rangeMin, rangeMax);
	}
	
	@Override
	public String toString() {
		return "Gaussian mutation";
	}
	
}
