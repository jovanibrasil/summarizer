package ai.fuzzy.optimization;

import java.util.ArrayList;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationDeltaJump;
import net.sourceforge.jFuzzyLogic.optimization.Parameter;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;


public class Optimization {

	public Optimization(String fileName, String[] varNames) {
		
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
		//
		//

		// Error function to be optimized
		ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(); 
		
		// OptimizationGradient and Optimization Partial Derivative
		OptimizationDeltaJump optimizationDeltaJump = new OptimizationDeltaJump(ruleBlock, errorFunction, parameterList); 
		optimizationDeltaJump.setMaxIterations(20);
		optimizationDeltaJump.setVerbose(true);
		optimizationDeltaJump.optimize(); // optimize using delta jump method
		
		// save optimized result
		System.out.println(ruleBlock.toStringFcl());
	    Gpr.toFile("flc/fb2015_optimized.flc", ruleBlock.toStringFcl());
		
	    // functionBlock.reset();
	    // JFuzzyChart.get().chart(functionBlock);
	    
	}
	
	
}
