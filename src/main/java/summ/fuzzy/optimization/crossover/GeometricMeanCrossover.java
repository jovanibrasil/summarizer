package summ.fuzzy.optimization.crossover;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.functions.Function;
import summ.utils.MathUtils;

/**
 * @author Jovani Brasil
 * 
 * Geometric mean crossover: the child is formed by the geometric mean between the 
 * selected parents (Davis 1991). 
 *
 */
public class GeometricMeanCrossover extends CrossoverOperator {

	public GeometricMeanCrossover(Function function) {
		super(function);
	}

	/**
	 * Generates a new chromossome with the geometric mean between two parents.
	 * 
	 * @param parent1
	 * @param parent2
	 * @return
	 */
	public Chromosome mean(Chromosome parent1, Chromosome parent2) {
		Chromosome child = new Chromosome();
		RealVector result = parent1.getGenes().ebeMultiply(
				parent2.getGenes());
		result = MathUtils.ebeSqrt(result);
		child.setGenes(result);
		return child;
	}
	
	@Override
	public String toString() {
		return "Geometric mean crossover";
	}

	@Override
	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2) {
		return Arrays.asList(mean(parent1, parent2));
	}
	
}
