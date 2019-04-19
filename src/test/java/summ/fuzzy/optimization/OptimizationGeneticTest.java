package summ.fuzzy.optimization;

import org.junit.jupiter.api.Test;

import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenBell;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
//import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
//import net.sourceforge.jFuzzyLogic.rule.Variable;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptimizationGeneticTest {

	private static final double DELTA = 1e-6;

	/*
	 * 
	 * a = 0.5 b = 2.0 mean = 0.6
	 * 
	 */
	@Test
	void testConvertVariableToCustomVariable() {
		Variable var = new Variable("var");
		LinguisticTerm lt = new LinguisticTerm("lt", new MembershipFunctionGenBell(
				new Value(0.5), new Value(2.0), new Value(0.6))); 
		var.add(lt);
		OptimizationGenetic og = new OptimizationGenetic(null, null, null, null, null);
		
		CustomVariable customVar = og.convertVariableToCustomVariable(var);
		CustomLinguisticTerm customLt = customVar.getLinguisticTerm("lt");
		assertEquals(0.5, customLt.getParameter(0));
		assertEquals(2.0, customLt.getParameter(1));
		assertEquals(0.6, customLt.getParameter(2));
	}
	
	@Test
	void testEvaluatePopulation() {
		
	}
	
	@Test
	void testGenerateFirstPopulation() {
		
	}
	
	@Test
	void testGenerateIntermediatePopulation() {
		
	}
	
}
