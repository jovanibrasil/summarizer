package summ.fuzzy.optimization.mutation;

public class UniformMutation extends MutationOperator {

	public UniformMutation() {
		super();
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
