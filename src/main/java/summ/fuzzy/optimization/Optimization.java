package summ.fuzzy.optimization;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.optimization.crossover.BlxCrossover;
import summ.fuzzy.optimization.crossover.CrossoverOperator;
import summ.fuzzy.optimization.evaluation.ErrorFunctionSummarization;
import summ.fuzzy.optimization.mutation.MutationOperator;
import summ.fuzzy.optimization.mutation.UniformMutation;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;


public class Optimization {

	private final int MAX_ITERATIONS = 20;
	
	private final double MUTATION_PROBABILITY = 0.1;
	private final double CROSSOVER_PROBABILITY = 0.6;
	private final boolean ELITISM = false;
	private final int POPULATION_SIZE = 10;
	
	private static final Logger log = LogManager.getLogger(Optimization.class);
	
	public Optimization(String fileName, String[] varNames, Text originalText, Text referenceSummary, Evaluation evaluation) {
		
		FIS fis = FIS.load(fileName);
		
	    ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(fileName, originalText, referenceSummary, evaluation); 
	    log.info("Error function: " + errorFunction);
		
		CrossoverOperator crossoverOperator = new BlxCrossover();
		log.info("Crossover operator: " + crossoverOperator);
		
		MutationOperator mutationOperator = new UniformMutation();
		log.info("Mutation operator: " + mutationOperator);

		log.info("Crossover probability: " + CROSSOVER_PROBABILITY);
		log.info("Mutation probability: " + MUTATION_PROBABILITY);
		log.info("Elitism: " + ELITISM);
		log.info("Population size: " + POPULATION_SIZE);
		RuleBlock ruleBlock = fis.getFunctionBlock(null).getFuzzyRuleBlock(null);
		List<String> variables = Arrays.asList("k1", "k2", "loc_len", "informatividade");
		log.info("Optimization variables: " + variables);
		OptimizationGenetic geneticOptimization = new OptimizationGenetic(ruleBlock, errorFunction, crossoverOperator, 
			mutationOperator, CROSSOVER_PROBABILITY, MUTATION_PROBABILITY, ELITISM, POPULATION_SIZE, variables);

		log.info("Max iterations: " + MAX_ITERATIONS);
		geneticOptimization.setMaxIterations(MAX_ITERATIONS);
		geneticOptimization.setVerbose(false);
		geneticOptimization.optimize(); 
		
		// save optimized result
		//		System.out.println(ruleBlock.toStringFcl());
		//	    Gpr.toFile("flc/fb2015_optimized.flc", ruleBlock.getFunctionBlock().toString() + ruleBlock.toString() );
		//		
	    // functionBlock.reset();
	    // JFuzzyChart.get().chart(functionBlock);
	    
	}
	
	
}
