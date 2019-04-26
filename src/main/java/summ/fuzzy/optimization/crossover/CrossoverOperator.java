package summ.fuzzy.optimization.crossover;

import java.util.List;

import summ.fuzzy.optimization.Chromosome;

public interface CrossoverOperator {

	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2);
	
}
