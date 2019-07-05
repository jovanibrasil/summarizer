package summ.fuzzy.optimization.crossover;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.functions.Function;

public class NpointInterleaveCrossover extends CrossoverOperator {

	int points;
	
	public NpointInterleaveCrossover(int points, Function function) {
		super(function);
		this.points = points;
	}
	
	/**
	 * Returns a list of chromossome created with n-point interleave operation.
	 * 
	 * @param parent1
	 * @param parent2
	 * @return
	 */
	public List<Chromosome> nPointInterleave(Chromosome parent1, Chromosome parent2) {
		
		Chromosome child1 = new Chromosome();
		Chromosome child2 = new Chromosome();

		int middle = parent1.getGenes().getDimension() / 2;

		RealVector child1Chromossome = (parent1.getGenes().getSubVector(0, middle))
				.append(parent2.getGenes().getSubVector(middle, parent2.getGenes().getDimension()));
		RealVector child2Chromossome = (parent2.getGenes().getSubVector(0, middle))
				.append(parent1.getGenes().getSubVector(middle, parent1.getGenes().getDimension()));
		
		child1.setGenes(child1Chromossome);
		child2.setGenes(child2Chromossome);
		
		return Arrays.asList(child1, child2);
	}

	@Override
	public String toString() {
		return "Npoint Interleave crossover";
	}
	
	@Override
	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2) {
		return nPointInterleave(parent1, parent2);
	}
	
}
