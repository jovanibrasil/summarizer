package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.CustomLinguisticTerm;
import summ.fuzzy.optimization.CustomVariable;
import summ.fuzzy.optimization.functions.FunctionDetails;

public interface MutationOperator {
	
	double getAleatoryFeasibleCoefficient(double rangeMin, double rangeMax);

	public default Chromosome mutateAllGenes(Chromosome chromosome, MutationOperator mutation) {
		for (CustomVariable variable : chromosome.getVariables()) {
			for (CustomLinguisticTerm term : variable.getLinguisticTerms()) {
				for (int index = 0; index < term.getParametersLength(); index++) {
					FunctionDetails funcInfo = term.getFunction().getFunctionInfo();
					// uniform
					term.setParameter(index, mutation.getAleatoryFeasibleCoefficient(funcInfo.getRangeMin(index), 
							funcInfo.getRangeMax(index)));
				}
			}
		}
		return chromosome;
	}
		
}
