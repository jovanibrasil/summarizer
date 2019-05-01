package summ.fuzzy.optimization.mutation;

import java.util.Random;

public class NonUniformMutation implements MutationOperator {
	
	private Random rand;
	
	public NonUniformMutation() {
		this.rand = new Random(); 
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
	public double getAleatoryFeasibleCoefficient(double rangeMin, double rangeMax) {
		return nonUniform(0, 5, 5, 0.5, rangeMin, rangeMax);
	}	
	
	@Override
	public String toString() {
		return "Non-uniform mutation";
	}
	
}
