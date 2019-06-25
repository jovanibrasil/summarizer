package summ.fuzzy.optimization.crossover;

import java.util.List;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.functions.Function;

public abstract class CrossoverOperator {

	public Function function;
	
	public CrossoverOperator(Function function) {
		this.function = function;
	}
	
	public abstract List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2);
	
}
