package summ.fuzzy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenBell;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

class FuzzySystemTest {

	private static final double DELTA = 1e-6;

	@Test
	void testFuzzySystemLoadVariabletf_isfValues() {
		FuzzySystem fs = new FuzzySystem("fcl/fb2015.fcl");
		Variable var = fs.getVariable("tf_isf");
		LinguisticTerm lTerm = var.getLinguisticTerm("baixo");
		MembershipFunction mf = lTerm.getMembershipFunction();
		// TERM baixo := gbell 0.244 2.410 0.008;
		assertEquals(0.008, mf.getParameter(0));
		assertEquals(0.244, mf.getParameter(1));
		assertEquals(2.410, mf.getParameter(2));
	}

	@Test
	void testFuzzySystemSetVariabletf_isfValues() {
		FuzzySystem fs = new FuzzySystem("fcl/fb2015.fcl");
		Variable var = fs.getVariable("tf_isf");
		LinguisticTerm lTerm = var.getLinguisticTerm("baixo");
		lTerm.setMembershipFunction(new MembershipFunctionGenBell(new Value(0.3), new Value(0.3), new Value(3.0))); // (a, b, mean)
		fs.setVariable("tf_isf", var);
		var = fs.getVariable("tf_isf");
		lTerm = var.getLinguisticTerm("baixo");
		MembershipFunction mf = lTerm.getMembershipFunction();
		assertEquals(3.0, mf.getParameter(0)); // mean
		assertEquals(0.3, mf.getParameter(1)); // a
		assertEquals(0.3, mf.getParameter(2)); // b
	}

	// test triangular and trapezoidal functions
	@Test
	void testConfortabilityWithCogDefuzz() {
		FuzzySystem fs = new FuzzySystem("fcl/confortability.fcl");
		fs.setInputVariable("temperature", 25.0);
		fs.setInputVariable("humidity", 80.0);
		fs.setOutputVariable("confortability");
		fs.setDefuzzificationType(DefuzzifierType.COG);
		Variable outputValue = fs.evaluate(); // evaluate (run the system)
		assertEquals(5.0, outputValue.getValue(), DELTA); // centroid (center of gravity)
	}

	// test triangular and trapezoidal functions
	@Test
	void testConfortabilityWithMomDefuzz() {

		FuzzySystem fs = new FuzzySystem("fcl/confortability.fcl");
		fs.setInputVariable("temperature", 25.0);
		fs.setInputVariable("humidity", 80.0);
		fs.setOutputVariable("confortability");

		fs.setDefuzzificationType(DefuzzifierType.MOM);
		Variable outputValue = fs.evaluate(); // evaluate (run the system)
		assertEquals(5.0, outputValue.getValue()); // mom

	}

	// test gaussian functions
	@Test
	void testSentenceInformativityWithCogDefuzz() {

		FuzzySystem fs = new FuzzySystem("fcl/fb2015.fcl");
		fs.setInputVariable("loc_len", 0.5);
		fs.setInputVariable("tf_isf", 0.8);
		fs.setInputVariable("title_words_relative", 0.4);
		fs.setOutputVariable("informativity");

		fs.setDefuzzificationType(DefuzzifierType.COG);

		Variable outputValue = fs.evaluate(); // evaluate (run the system)
		assertEquals(0.7665974171965814, outputValue.getValue()); // using center of gravity (COG, centroid)

	}

	// test gaussian functions
	@Test
	void testSentenceInformativityWithMomDefuzz() {

		FuzzySystem fs = new FuzzySystem("fcl/fb2015.fcl");
		fs.setInputVariable("loc_len", 0.5);
		fs.setInputVariable("tf_isf", 0.8);
		fs.setInputVariable("title_words_relative", 0.4);
		fs.setOutputVariable("informativity");
		fs.setDefuzzificationType(DefuzzifierType.MOM);
		Variable outputValue = fs.evaluate(); // evaluate (run the system)
		assertEquals(0.8969999999999999, outputValue.getValue(), DELTA); // using mean of maximum (MOM)

	}
	
	@Test
	void tipperSystem() {
		FuzzySystem fs = new FuzzySystem("fcl/tipper.fcl");
		fs.setInputVariable("service", 3.0);
		fs.setInputVariable("food", 7.0);
		fs.setOutputVariable("tip");
		Variable outputValue = fs.evaluate();
		assertEquals(11.701603788, outputValue.getValue(), DELTA);
	}

}
