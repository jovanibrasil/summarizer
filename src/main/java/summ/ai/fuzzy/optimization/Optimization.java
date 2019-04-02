package summ.ai.fuzzy.optimization;

import java.util.ArrayList;

import org.apache.commons.math3.genetics.GeneticAlgorithm;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationDeltaJump;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationGradient;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationPartialDerivate;
import net.sourceforge.jFuzzyLogic.optimization.Parameter;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.ai.fuzzy.optimization.crossover.SimpleMean;
import summ.model.Text;


public class Optimization {

	public Optimization(String fileName, String[] varNames, Text originalText, Text referenceSummary) {
		
		FIS fis = FIS.load(fileName);
		FunctionBlock functionBlock = fis.getFunctionBlock(null);
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
		ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(fileName, originalText, referenceSummary); 
		
		// Define the crossover operator
		SimpleMean sm = new SimpleMean();
		
		// OptimizationGradient Optimization Partial Derivative Optimization Gradient
		//OptimizationDeltaJump optimization = new OptimizationDeltaJump(ruleBlock, errorFunction, parameterList); 
		OptimizationGenetic optimization = new OptimizationGenetic(ruleBlock, errorFunction, parameterList, sm);
		//OptimizationGradient 
		//OptimizationGradient optimization = new OptimizationGradient(ruleBlock, errorFunction, parameterList); 

		//OptimizationPartialDerivate
		//OptimizationPartialDerivate optimization = new OptimizationPartialDerivate(ruleBlock, errorFunction, parameterList); 
		
		optimization.setMaxIterations(20);
		optimization.setVerbose(true);
		optimization.optimize(); // optimize using delta jump method
		
		// save optimized result
		System.out.println(ruleBlock.toStringFcl());
	    Gpr.toFile("flc/fb2015_optimized.flc", ruleBlock.getFunctionBlock().toString() + ruleBlock.toString() );
		
	    // functionBlock.reset();
	    // JFuzzyChart.get().chart(functionBlock);
	    
	}
	
	
}
