package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.functions.Function;

public class LimitMutation extends MutationOperator {
	
	private Function function;
	
	public LimitMutation(Function function) {
		super();
		this.function = function;
	}
	
	/**
	 * Substitui o gene por um dos valores limites dos intervalos dos genes. Ideal para reduzir a 
	 * perda de diversidade que certos operadores de cruzamento produzem, como o cruzamento aritmetico,
	 * que leva os genes para o centro dos intervalos permitidos.
	 * 
	 * Referencia: Michalewicz, 1994
	 * 
	 */
	public double limit(double rangeMin, double rangeMax) {
		if(rand.nextFloat() < 0.5) {
			return rangeMin;
		} else {
			return rangeMax;
		}
	}

	@Override
	public double getAleatoryFeasibleCoefficient(int index) {
		return limit(function.getRangeMin(index), function.getRangeMax(index));
	}
	
	@Override
	public String toString() {
		return "Limit mutation";
	}

	@Override
	public double getAleatoryFeasibleCoefficient(double min, double max) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
