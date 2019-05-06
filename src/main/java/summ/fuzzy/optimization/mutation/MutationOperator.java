package summ.fuzzy.optimization.mutation;

import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.CustomLinguisticTerm;
import summ.fuzzy.optimization.CustomVariable;
import summ.fuzzy.optimization.functions.FunctionDetails;

public abstract class MutationOperator {
	
	private static final Logger log = LogManager.getLogger(MutationOperator.class);
	
	public Random rand;
	
	public MutationOperator() {
		this.rand = new Random();
	}
	
	public abstract double getAleatoryFeasibleCoefficient(double rangeMin, double rangeMax);
	
	public double getMutatedFeasibleCoefficient(int index, double rangeMin, double rangeMax, CustomVariable variable, CustomLinguisticTerm term) {
		double value;
		int loopControl = 0;
		while(true) {
			value = this.getAleatoryFeasibleCoefficient(rangeMin, rangeMax);
			
			if(term.getFunction().isFeasibleValue(index, term.getTermName(), value, variable)) break;
			if(loopControl == 100) {
				log.error("Coefficient generation error ...");
				value = term.getParameter(2); // não foi possível, então mantém o valor anterior
				break;
			}
			loopControl++; // tenta 100x gerar um novo coeficiente
		}	
		return value;
	}
	
	public Chromosome mutateGenes(Chromosome chromosome, double geneMutationProbability) {
		for (CustomVariable variable : chromosome.getVariables()) {
			for (CustomLinguisticTerm term : variable.getLinguisticTerms()) {
				if(this.rand.nextDouble() < geneMutationProbability) {
					for (int index = 0; index < term.getParametersLength(); index++) {
						FunctionDetails funcInfo = term.getFunction().getFunctionInfo();
						term.setParameter(index, this.getMutatedFeasibleCoefficient(index, funcInfo.getRangeMin(index), funcInfo.getRangeMax(index), variable, term));
					}
				}
			}
		}
		return chromosome;
	}
		
}
