package summ.fuzzy.optimization.crossover;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.functions.Function;

/**
 * @author Jovani Brasil
 * 
 * Simple mean crossover: the generated child is formed by the mean values between the
 * selected parents (Davis 1991).
 * 
 */
public class SimpleMeanCrossover extends CrossoverOperator {

	public SimpleMeanCrossover(Function function) {
		super(function);
	}
	
	/**
	 * Returns a new chromossome with the simple mean between the parents genes.
	 * 
	 * @param parent1
	 * @param parent2
	 * @return a new chromossome.
	 */
	public Chromosome mean(Chromosome parent1, Chromosome parent2) {
		Chromosome child = new Chromosome();
		// compute the mean between the parent1 and parent 2 terms
		RealVector result = parent1.getGenes().add(parent2.getGenes()).mapDivide(2); 
		child.setGenes(result);
		return child;
	}

	@Override
	public String toString() {
		return "Simple mean crossover";
	}
	
	@Override
	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2) {
		return Arrays.asList(mean(parent1, parent2));
	}
	
}
