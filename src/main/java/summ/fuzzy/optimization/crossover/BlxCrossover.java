package summ.fuzzy.optimization.crossover;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import summ.fuzzy.optimization.BellFunction;
import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.CustomLinguisticTerm;
import summ.fuzzy.optimization.CustomVariable;

public class BlxCrossover implements Crossover {

public CustomLinguisticTerm generateBlxTerm(CustomLinguisticTerm term1, CustomLinguisticTerm term2, double alpha) {
		
		CustomLinguisticTerm term = new CustomLinguisticTerm(term1.getParametersLength(), term1.getTermName());
		
		Random rand = new Random();
		for (int idx = 0; idx < term1.getParametersLength(); idx++) {
			
            // TODO variáveis de tamanho diferente lançam erro
        	// = [varp1[idx] + beta * (varp2[idx] - varp1[idx]) 
            
			double p1Coef = term1.getParameter(idx);
			double p2Coef = term2.getParameter(idx);
			
        	double d = Math.abs(p1Coef - p2Coef);
            double rangeMin = Math.min(p1Coef, p2Coef) - alpha * d;
            double rangeMax = Math.max(p1Coef, p2Coef) + alpha * d;
            
            
            int iterationControl = 0;
            double value = 0;
            while(true) { // while the solution is not feasible
            	
            	if (iterationControl == 100) {
                    //logger.error("Não foi possível gerar genes factíveis com o blx-alpha")
                    value = rand.nextBoolean() ? p1Coef : p2Coef; 
            		break;
                }
            	
            	// nextDouble returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0
                value = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
                
            	if(BellFunction.isFeasibleValue(idx, value)) { // break the loop if the solution if feasible
            		break;
            	}
                iterationControl+=1;
            }
    	}
		return term;
	}
	
	/*
	 * O crossover blx-alpha ou crossover mistura (blend crossover) beta pertence a
	 * uma distribuição uniforme entre -alpha e 1+alpha o alpha extende o intervalo
	 * para ambos os lados
	 * 
	 * # TODO variáveis de tamanho diferente lançam erro
	 */
	public Chromosome blxAlpha(Chromosome parent1, Chromosome parent2) {
		
		Random rand = new Random();
		double alpha = 0.4; 
        double loc = 1; 
        double beta = (-alpha) + ((loc+alpha) - (-alpha)) * rand.nextDouble();
        
        Chromosome child = new Chromosome();
		List<CustomVariable> p1Variables = parent1.getVariables();
		List<CustomVariable> p2Variables = parent2.getVariables();

		for (int i = 0; i < p1Variables.size(); i++) {
			CustomVariable variable = new CustomVariable(p1Variables.get(i).getName()); 
			List<CustomLinguisticTerm> p1Terms = p1Variables.get(i).getLinguisticTerms();
			List<CustomLinguisticTerm> p2Terms = p2Variables.get(i).getLinguisticTerms();
			for (int j = 0; j < p1Terms.size(); j++) {
				CustomLinguisticTerm term = generateBlxTerm(p1Terms.get(j), 
						p2Terms.get(j), alpha);
				
				variable.addLinguisticTerm(term);
			}
			child.addGene(variable);
		}
		return child;
	}

	@Override
	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2) {
		return Arrays.asList(blxAlpha(parent1, parent2));
	}
	
}
