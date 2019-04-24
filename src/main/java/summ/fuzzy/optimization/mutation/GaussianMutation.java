package summ.fuzzy.optimization.mutation;

import java.util.Random;

public class GaussianMutation implements Mutation {

	private Random rand;
	
	public GaussianMutation() {
		this.rand = new Random(); 
	}
	
	/**
	 * Substitui um gene por um valor aleatório de uma distribuição normal, dentro dos limites do 
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
	
}
