package summ.fuzzy.optimization;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.genetics.GeneticAlgorithm;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationDeltaJump;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationGradient;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationPartialDerivate;
import net.sourceforge.jFuzzyLogic.optimization.Parameter;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.optimization.crossover.SimpleMean;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;


public class Optimization {

	public Optimization(String fileName, String[] varNames, Text originalText, Text referenceSummary, Evaluation evaluation) {
		
		FIS fis = FIS.load(fileName);
		RuleBlock ruleBlock = fis.getFunctionBlock(null).getFuzzyRuleBlock(null);
		
		ArrayList<Parameter> parameterList = new ArrayList<Parameter>(); //array of parameters to optimize 
		
	    // Add variables that will be optimized.
	    for (String name : varNames) {
			parameterList.addAll(Parameter.parametersMembershipFunction(ruleBlock.getVariable(name)));	
		}
		
	    // Add every rule's weight
//	    for( Iterator it = fuzzyRuleSet.getRules().iterator(); it.hasNext(); ) {
//	      Rule rule = (Rule) it.next();
//	      Parameter.parameterListAddRule(parameterList, rule);
//	    }		
		//		//		for (Rule rule : ruleBlock.getRules())
		//			parameterList.addAll(Parameter.parametersRuleWeight(rule));

		// Error function to be optimized
		ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(fileName, originalText, referenceSummary, evaluation); 
		
		// Define the crossover operator
		SimpleMean sm = new SimpleMean();
		
		// OptimizationGradient Optimization Partial Derivative Optimization Gradient
		//OptimizationDeltaJump optimization = new OptimizationDeltaJump(ruleBlock, errorFunction, parameterList); 
		OptimizationGenetic geneticOptimization = new OptimizationGenetic(ruleBlock, errorFunction,
				parameterList, sm, Arrays.asList("k1", "k2", "loc_len", "informatividade"));
		//OptimizationGradient 
		//OptimizationGradient optimization = new OptimizationGradient(ruleBlock, errorFunction, parameterList); 

		//OptimizationPartialDerivate
		//OptimizationPartialDerivate optimization = new OptimizationPartialDerivate(ruleBlock, errorFunction, parameterList); 
		
		geneticOptimization.setMaxIterations(20);
		geneticOptimization.setVerbose(true);
		geneticOptimization.optimize(); // optimize using delta jump method
		
		// save optimized result
//		System.out.println(ruleBlock.toStringFcl());
//	    Gpr.toFile("flc/fb2015_optimized.flc", ruleBlock.getFunctionBlock().toString() + ruleBlock.toString() );
//		
	    // functionBlock.reset();
	    // JFuzzyChart.get().chart(functionBlock);
	    
	}
	
	
}
