package summ.ai.fuzzy.optimization.crossover;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealVector;

import summ.ai.fuzzy.optimization.Chromosome;
import summ.ai.fuzzy.optimization.CustomLinguisticTerm;
import summ.ai.fuzzy.optimization.CustomVariable;

public class SimpleMean implements Crossover {

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
		List<CustomVariable> p1Variables = parent1.getVariables();
		List<CustomVariable> p2Variables = parent2.getVariables();

		for (int i = 0; i < p1Variables.size(); i++) {
			CustomVariable variable = new CustomVariable(); 
			List<CustomLinguisticTerm> p1Terms = p1Variables.get(i).getLinguisticTerms();
			List<CustomLinguisticTerm> p2Terms = p2Variables.get(i).getLinguisticTerms();
			for (int j = 0; j < p1Terms.size(); j++) {
				CustomLinguisticTerm term = new CustomLinguisticTerm();
				term.setTermName(p1Terms.get(j).getTermName());
				
				// simple mean ((varp1[idx] + varp2[idx])/2)
				RealVector result = p1Terms.get(j).getParameters()
					.add(p2Terms.get(j).getParameters()).mapDivide(2);
				
				term.appendParameter(result);
				variable.addLinguisticTerm(term);
			}
			child.addGene(variable);
		}
		return child;
	}

	@Override
	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2) {
		return Arrays.asList(mean(parent1, parent2));
	}
	
}
