package summ.fuzzy.optimization.crossover;

import java.util.Arrays;
import java.util.List;

import summ.fuzzy.optimization.Chromosome;

public class NpointInterleaveCrossover implements CrossoverOperator {

	int points;
	
	public NpointInterleaveCrossover(int points) {
		this.points = points;
	}
	
	/*
	 * Crossover com simples intercalação de valores TODO implementar o parâmetro
	 * points. Deve ser o novo i, porém são necessárias validações de intervalo. 1)
	 * Cruzamento uniponto, com ponto fixo. 2) TODO Cruzamento uniponto com ponto
	 * aleatório. 3) Atualmente o cruzamento é multiponto, com intervalo definido
	 * pela variável i.
	 * 
	 * Assume Solutions with the same size
	 * 
	 */
	public List<Chromosome> nPointInterleave(Chromosome parent1, Chromosome parent2) {
		
		Chromosome child1 = new Chromosome();
		Chromosome child2 = new Chromosome();

		int middle = parent1.getVariables().size() / 2;

		child1.appendGenes(parent1.getVariables().subList(0, middle));
		child1.appendGenes(parent2.getVariables().subList(middle, parent2.getVariables().size()));
		child2.appendGenes(parent2.getVariables().subList(0, middle));
		child2.appendGenes(parent1.getVariables().subList(middle, parent1.getVariables().size()));

		return Arrays.asList(child1, child2);
	}

	@Override
	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2) {
		return nPointInterleave(parent1, parent2);
	}

	@Override
	public String toString() {
		return "Npoint Interleave crossover";
	}
	
}
