package summ.fuzzy.optimization.mutation;

public class LimitMutation extends MutationOperator {
	
	public LimitMutation() {
		super();
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
	public double getAleatoryFeasibleCoefficient(double rangeMin, double rangeMax) {
		return limit(rangeMin, rangeMax);
	}
	
	@Override
	public String toString() {
		return "Limit mutation";
	}
	
}
