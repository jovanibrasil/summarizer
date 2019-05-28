package summ.fuzzy.optimization.mutation;

import java.util.Random;

import summ.fuzzy.optimization.functions.Function;

public class NonUniformMutation extends MutationOperator {
	
	private Random rand;
	private Function function;
	
	public NonUniformMutation(Function function) {
		this.rand = new Random(); 
		this.function = function;
	}
	
	/**
	 * Substitui um gene por um valor aleatorio de uma distribuicao nao uniforme.
	 *
	 * Referencia: Michalewicz, 1994
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
	
	/**
	 * Aplicacao do operador de mutacao nao uniforme em todos os genes do cromossomo. 
	 */
	public void nonUniformMultiple() {
		throw new UnsupportedOperationException("Operation not implemented yet");
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
	
}
