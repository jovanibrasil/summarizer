package summ.fuzzy.optimization.mutation;

import java.util.Random;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.CustomLinguisticTerm;
import summ.fuzzy.optimization.CustomVariable;
import summ.fuzzy.optimization.functions.FunctionDetails;

public abstract class MutationOperator {
	
	public Random rand;
	
	public MutationOperator() {
		this.rand = new Random();
	}
	
	public abstract double getAleatoryFeasibleCoefficient(double rangeMin, double rangeMax);

	public Chromosome mutateGenes(Chromosome chromosome, MutationOperator mutation, double geneMutationProbability) {
		for (CustomVariable variable : chromosome.getVariables()) {
			for (CustomLinguisticTerm term : variable.getLinguisticTerms()) {
				if(this.rand.nextDouble() < geneMutationProbability) {
					for (int index = 0; index < term.getParametersLength(); index++) {
						FunctionDetails funcInfo = term.getFunction().getFunctionInfo();
						term.setParameter(index, mutation.getAleatoryFeasibleCoefficient(funcInfo.getRangeMin(index), 
								funcInfo.getRangeMax(index)));
					}
				} 
			}
		}
		return chromosome;
	}
		
}
