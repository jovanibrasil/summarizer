package summ.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.defuzzifier.Defuzzifier;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfArea;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravity;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierLeftMostMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierMeanMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierRightMostMax;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzySystem {
	
	private FIS fis;
	private FunctionBlock functionBlock;
	private String outputVariableName;
	
	public FuzzySystem(String fileName) {
		this.fis = FIS.load(fileName, true);
		
		if(this.fis == null) {
			System.err.println("Can't load file: '" + fileName + "'");
	        return;
		}
		this.functionBlock = this.fis.getFunctionBlock(null);
	}
	
	public void showFuzzySystem(Variable outVariable) {
		JFuzzyChart.get().chart(functionBlock); // show fuzzy system
		//JFuzzyChart.get().chart(outVariable, outVariable.getDefuzzifier(), true); // show output 
		//System.out.println(this.fis); // print fuzzy system configuration 
	}
	
	public void showFuzzySystem() {
		JFuzzyChart.get().chart(functionBlock);	
		//System.out.println(this.fis); // print fuzzy system configuration 
	}
	
	public void setOutputVariable(String outputVariableName) {
		this.outputVariableName = outputVariableName;
	}
	
	public Variable getVariable(String variableName) {
		return this.functionBlock.getVariable(variableName);
	}
	
	public void setVariable(String variableName, Variable variable) {
		this.fis.getFunctionBlock(null).setVariable(variableName, variable);
	}

	public void setDefuzzificationType(DefuzzifierType deffuzifierType) {
		Variable outputVariable = this.fis.getFunctionBlock(null).getVariable(outputVariableName);
		Defuzzifier defuzzifier = null;
		switch (deffuzifierType) {
			case COG:
				defuzzifier = new DefuzzifierCenterOfGravity(outputVariable);
				break;
			case MOM:
				defuzzifier = new DefuzzifierMeanMax(outputVariable);
				break;
			case LMM:
				defuzzifier = new DefuzzifierLeftMostMax(outputVariable);
				break;
			case RMM:
				defuzzifier = new DefuzzifierRightMostMax(outputVariable);
				break;
			case COA:
				defuzzifier = new DefuzzifierCenterOfArea(outputVariable);
		}
		outputVariable.setDefuzzifier(defuzzifier);
	}

	public void setInputVariable(String varName, double value) {
		this.fis.setVariable(varName, value);
	}

	public Variable evaluate() {

		this.fis.evaluate();

//		for( Rule r : this.fis.getFunctionBlock(null).getFuzzyRuleBlock("No1").getRules() )
//		      System.out.println(r);
		
		Variable outputValue = this.functionBlock.getVariable(outputVariableName); // read output variable
		return outputValue;
	}
	
	@Override
	public String toString() {
		return this.fis.toStringFcl();
	}

	public RuleBlock getRuleBlock() {
		return this.functionBlock.getFuzzyRuleBlock(null);
	}

	public void saveFuzzySystem(String outputPath) {
		Gpr.toFile(outputPath, this.fis.getFunctionBlock(null).toString());
		JFuzzyChart.get().chart(functionBlock);	
	}
	
}
