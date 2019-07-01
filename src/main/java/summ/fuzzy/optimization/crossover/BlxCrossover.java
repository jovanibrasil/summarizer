package summ.fuzzy.optimization.crossover;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.optimization.Chromosome;
import summ.fuzzy.optimization.functions.Function;

/**
 * @author Jovani Brasil.
 * 
 * BLX-ALPHA Crossover (Blend Crossover)
 *
 */
public class BlxCrossover extends CrossoverOperator {

	private static final Logger log = LogManager.getLogger(BlxCrossover.class);
	private double alpha = 0.4;
	
	public BlxCrossover(Function function) {
		super(function);
	}

	public RealVector generateBlx(RealVector p1, RealVector p2, double alpha) {
		RealVector child = new ArrayRealVector(p1.getDimension());
		Random rand = new Random();
		for (int index = 0; index < p1.getDimension(); index++) {

			double p1Coef = p1.getEntry(index);
			double p2Coef = p2.getEntry(index);
			
			double d = Math.abs(p1Coef - p2Coef);
			double rangeMin = Math.min(p1Coef, p2Coef) - alpha * d;
			double rangeMax = Math.max(p1Coef, p2Coef) + alpha * d;

			int iterationControl = 0;
			double value = 0;
			while (true) { // while the solution is not feasible

				if (iterationControl == 1000) {
					log.trace("Não foi possível gerar genes factíveis com o blx-alpha");
					value = rand.nextBoolean() ? p1Coef : p2Coef;
					break;
				}

				// nextDouble returns the next pseudo random, uniformly distributed double value between 0.0 and 1.0
				value = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
				if (this.function.isFeasibleValue(index, value)) break; // break the loop if the solution if feasible
				
				iterationControl += 1;
			}
			child.addToEntry(index, value);
		}
		return child;
	}

	/**
	 * Generated a new chromossome where each gene is a uniformly distributed double 
	 * between an extended interval controlled by alpha.
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public Chromosome blxAlpha(Chromosome c1, Chromosome c2) {
		Chromosome child = new Chromosome();
		RealVector coefficients = generateBlx(c1.getGenes(), c2.getGenes(), alpha);
		child.setGenes(coefficients);
		return child;
	}

	@Override
	public List<Chromosome> executeCrossover(Chromosome parent1, Chromosome parent2) {
		return Arrays.asList(blxAlpha(parent1, parent2));
	}

	@Override
	public String toString() {
		return "Blx crossover";
	}
	
}
