package summ.fuzzy.optimization.crossover;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.functions.Function;

public class SimpleMeanCrossover extends CrossoverOperator {

	public SimpleMeanCrossover(Function function) {
		super(function);
	}

	/*
	 * Média simples: Implementação do crossover média (Davis 1991). Dados dois
	 * cromossomos c1 e c2, um cromossomo c é gerado como c=(c1+c2)/2. Média
	 * geométrica: Dados dois cromossomos c1 e c2, um cromossomo c é gerado por uma
	 * média geométrica da seguinte forma c=square_root(c1*c2). Segundo (Lacerda e
	 * carrvalho), uma vez que o método leva os genes para o meio do intervalo [a,
	 * b], é causada uma perda de diversidade. Lembrando que um gene que não
	 * respeita o seu intervalo é dito infactível. TODO variáveis de tamanho
	 * diferente lançam erro
	 *
	 */
	public Chromosome mean(Chromosome parent1, Chromosome parent2) {
		Chromosome child = new Chromosome();
		// compute the mean between the parent1 and parent 2 terms
		// simple mean ((varp1[idx] + varp2[idx])/2)
		RealVector result = parent1.getGenes().add(parent2.getGenes()).mapDivide(2); 
		child.setGenes(result);
		return child;
	}

	@Override
	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2) {
		return Arrays.asList(mean(parent1, parent2));
	}
	
	@Override
	public String toString() {
		return "Simple mean crossover";
	}
	
}
