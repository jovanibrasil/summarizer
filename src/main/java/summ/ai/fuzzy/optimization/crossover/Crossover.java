package summ.ai.fuzzy.optimization.crossover;

import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.RealVector;

import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import summ.ai.fuzzy.optimization.BellFunction;
import summ.ai.fuzzy.optimization.CustomLinguisticTerm;
import summ.ai.fuzzy.optimization.CustomVariable;
import summ.ai.fuzzy.optimization.Chromosome;
import summ.utils.MathUtils;
import summ.utils.Tuple;

public interface Crossover {

	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2);
	
}
