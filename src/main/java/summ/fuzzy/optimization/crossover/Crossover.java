package summ.fuzzy.optimization.crossover;

import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.RealVector;

import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import summ.fuzzy.optimization.BellFunction;
import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.CustomLinguisticTerm;
import summ.fuzzy.optimization.CustomVariable;
import summ.utils.MathUtils;
import summ.utils.Tuple;

public interface Crossover {

	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2);
	
}
