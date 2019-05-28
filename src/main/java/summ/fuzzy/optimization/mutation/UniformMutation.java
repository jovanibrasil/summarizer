package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.functions.Function;

public class UniformMutation extends MutationOperator {

	private Function function;
	
	public UniformMutation(Function function) {
		super();
		this.function = function;
	}
	
	/**
	 * Substitui um gene por um valor aleatorio de uma distribuicao uniforme, dentro os limites do 
	 * interavalo permitido para cada gene.
	 */
	public double uniform(double rangeMin, double rangeMax) {
		return rangeMin + (rangeMax - rangeMin) * this.rand.nextDouble(); // uniformly distributed double
	}

	@Override
	public double getAleatoryFeasibleCoefficient(int index) {
		return uniform(function.getRangeMin(index), function.getRangeMax(index));
	}
	
	@Override
	public String toString() {
		return "Uniform mutation";
	}
	
}
