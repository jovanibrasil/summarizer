package summ.fuzzy.optimization.mutation;

import java.util.Random;

public class UniformMutation implements MutationOperator {

	private Random rand;
	
	public UniformMutation() {
		this.rand = new Random(); 
	}
	
	/**
	 * Substitui um gene por um valor aleatorio de uma distribuicao uniforme, dentro os limites do 
	 * interavalo permitido para cada gene.
	 */
	public double uniform(double rangeMin, double rangeMax) {
		return rangeMin + (rangeMax - rangeMin) * this.rand.nextDouble(); // uniformly distributed double
	}

	@Override
	public double getAleatoryFeasibleCoefficient(double rangeMin, double rangeMax) {
		return uniform(rangeMin, rangeMax);
	}
	
	@Override
	public String toString() {
		return "Uniform mutation";
	}
	
}
