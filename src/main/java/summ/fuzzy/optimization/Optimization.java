package summ.fuzzy.optimization;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.FuzzySystem;
import summ.fuzzy.optimization.crossover.BlxCrossover;
import summ.fuzzy.optimization.crossover.CrossoverOperator;
import summ.fuzzy.optimization.evaluation.ErrorFunctionSummarization;
import summ.fuzzy.optimization.mutation.GaussianMutation;
import summ.fuzzy.optimization.mutation.MutationOperator;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;


public class Optimization {

	private final int MAX_ITERATIONS = 60;
	private final double MUTATION_PROBABILITY = 0.1;
	private final double CROSSOVER_PROBABILITY = 0.6;
	private final boolean ELITISM = true;
	private final int POPULATION_SIZE = 100;
	private static final Logger log = LogManager.getLogger(Optimization.class);
	private OptimizationGenetic geneticOptimization;
	
	public Optimization(String fileName, List<Text> texts, List<Text> refSummaries, Evaluation evaluation, List<String> varNames) {
		
		FuzzySystem fs = new FuzzySystem(fileName);
		fs.setOutputVariable(varNames.get(varNames.size()-1));
		
	    ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(fs, texts, refSummaries, evaluation, varNames); 
	    log.info("Error function: " + errorFunction);
		CrossoverOperator crossoverOperator = new BlxCrossover();
		log.info("Crossover operator: " + crossoverOperator);		
		MutationOperator mutationOperator = new GaussianMutation();
		log.info("Mutation operator: " + mutationOperator);

		log.info("Crossover probability: " + CROSSOVER_PROBABILITY);
		log.info("Mutation probability: " + MUTATION_PROBABILITY);
		log.info("Elitism: " + ELITISM);
		log.info("Population size: " + POPULATION_SIZE);
		
		FIS fis = FIS.load(fileName);
		RuleBlock ruleBlock = fis.getFunctionBlock(null).getFuzzyRuleBlock(null);
		log.info("Optimization variables: " + varNames);
		this.geneticOptimization = new OptimizationGenetic(ruleBlock, errorFunction, crossoverOperator, 
			mutationOperator, CROSSOVER_PROBABILITY, MUTATION_PROBABILITY, ELITISM, POPULATION_SIZE, varNames);

		log.info("Max iterations: " + MAX_ITERATIONS);
		geneticOptimization.setMaxIterations(MAX_ITERATIONS);
		geneticOptimization.setVerbose(false);
		
	}
	
	public void showResult() {
		// save optimized result
		//		System.out.println(ruleBlock.toStringFcl());
		//	    Gpr.toFile("flc/fb2015_optimized.flc", ruleBlock.getFunctionBlock().toString() + ruleBlock.toString() );
		//		
	    // functionBlock.reset();
	    // JFuzzyChart.get().chart(functionBlock);
	}
	
	public void run() {
		this.geneticOptimization.optimize(); 
	}
	
}
