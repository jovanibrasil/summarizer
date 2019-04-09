package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.BellFunction;
import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.CustomLinguisticTerm;
import summ.fuzzy.optimization.CustomVariable;

public class Mutation {

	public Chromosome executeMutation(Chromosome chromosome) {
		for (CustomVariable variable : chromosome.getVariables()) {
			for (CustomLinguisticTerm term : variable.getLinguisticTerms()) {
				for (int index = 0; index < term.getParametersLength(); index++) {
					term.setParameter(index, BellFunction.getAleatoryFeasibleValue(index));		
				}
			}
		}
		return chromosome;
	}
	
}
