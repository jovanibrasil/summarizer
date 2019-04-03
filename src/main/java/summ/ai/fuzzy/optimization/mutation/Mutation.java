package summ.ai.fuzzy.optimization.mutation;

import summ.ai.fuzzy.optimization.BellFunction;
import summ.ai.fuzzy.optimization.Chromosome;
import summ.ai.fuzzy.optimization.CustomLinguisticTerm;
import summ.ai.fuzzy.optimization.CustomVariable;

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
