package summ.fuzzy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.defuzzifier.Defuzzifier;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfArea;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierCenterOfGravity;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierLeftMostMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierMeanMax;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierRightMostMax;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenBell;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzySystem {
	
	private static final Logger log = LogManager.getLogger(FuzzySystem.class);
	
	private FIS fis;
	private FunctionBlock functionBlock;
	private String outputVariableName;

	private RealVector coefficients;
	private List<String> varNames;
	private List<String> termNames;
	
	public FuzzySystem(String fileName) {
		
		this.fis = FIS.load(fileName, true);
		
		if(this.fis == null) {
			System.err.println("Can't load file: '" + fileName + "'");
	        return;
		}
		
		this.coefficients = new ArrayRealVector();
		this.varNames = new ArrayList<>();
		this.termNames = Arrays.asList("baixo", "medio", "alto");
		this.functionBlock = this.fis.getFunctionBlock(null);
		
		this.functionBlock.getVariables().values().forEach(v -> {
			varNames.add(v.getName());
//			if(termNames.isEmpty()) {
//				v.getLinguisticTerms().values().forEach(lt -> { termNames.add(lt.getTermName()); });	
//			}
			termNames.forEach(termName -> {
				MembershipFunction mf = v.getMembershipFunction(termName);
				log.debug("Loading gbell fuzzy membership (a, b, c) = (" + mf.getParameter(1) + ", " + mf.getParameter(2) + ", " + mf.getParameter(0) + ")");
				coefficients = coefficients.append(mf.getParameter(1))
					.append(mf.getParameter(2))
					.append(mf.getParameter(0));
			});
			
		});
		
		log.debug("Loaded Variables: " + this.varNames.toString());
		log.debug("Loaded Terms: " + this.termNames.toString());
		
	}

	public void getCoefficientsVectorFromVariables() {

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
		Variable outputValue = this.functionBlock.getVariable(outputVariableName); // read output variable
		
//		for( Rule r : this.fis.getFunctionBlock(null).getFuzzyRuleBlock("No1").getRules() )
//			System.out.println(r);
		
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
	}
	
	public void setValue(int varIndex, int termIndex, double a, double b, double c) {

		Variable var = getVariable(varNames.get(varIndex));
		LinguisticTerm lTerm = var.getLinguisticTerm(termNames.get(termIndex));
		lTerm.setMembershipFunction(new MembershipFunctionGenBell(
				new Value(a), // a
				new Value(b), // b
				new Value(c))); // mean						
		setVariable(varNames.get(varIndex), var);
		
	}
	
	public void setCoefficients(RealVector coefficients) {
		this.coefficients = coefficients;
		// update fuzzy system
		for (int vIdx = 0; vIdx < varNames.size(); vIdx++) {
			Variable var = getVariable(varNames.get(vIdx));
			int vInitIdx = vIdx * 9;
			for (int tIdx = 0; tIdx < termNames.size(); tIdx++) {
				LinguisticTerm lTerm = var.getLinguisticTerm(termNames.get(tIdx));

				int cIdx = vInitIdx + tIdx * 3;
				
				lTerm.setMembershipFunction(new MembershipFunctionGenBell(
						new Value(coefficients.getEntry(cIdx)), // a
						new Value(coefficients.getEntry(cIdx + 1)), // b
						new Value(coefficients.getEntry(cIdx + 2)))); // mean
			}
			setVariable(varNames.get(vIdx), var);
		}	
	}

	public RealVector getCoefficients() {
		return this.coefficients;
	}
	
}
