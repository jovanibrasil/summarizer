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

public class BlxCrossover extends CrossoverOperator {

	private static final Logger log = LogManager.getLogger(CrossoverOperator.class);
	
	public BlxCrossover(Function function) {
		super(function);
	}

	public RealVector generateBlx(RealVector p1, RealVector p2, double alpha) {
		RealVector child = new ArrayRealVector(p1.getDimension());
		Random rand = new Random();
		for (int index = 0; index < p1.getDimension(); index++) {

			// TODO variáveis de tamanho diferente lançam erro
			double p1Coef = p1.getEntry(index);
			double p2Coef = p2.getEntry(index);
			
			double d = Math.abs(p1Coef - p2Coef);
			double rangeMin = Math.min(p1Coef, p2Coef) - alpha * d;
			double rangeMax = Math.max(p1Coef, p2Coef) + alpha * d;

			int iterationControl = 0;
			double value = 0;
			while (true) { // while the solution is not feasible

				if (iterationControl == 1000) {
					//log.error("Não foi possível gerar genes factíveis com o blx-alpha");
					value = rand.nextBoolean() ? p1Coef : p2Coef;
					break;
				}

				// nextDouble returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0
				value = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();

				if (this.function.isFeasibleValue(index, value)) { // break the loop if the solution if feasible
					break;
				}
				
				iterationControl += 1;
			}
			child.addToEntry(index, value);
		}
		return child;
	}

	/*
	 * O crossover blx-alpha ou crossover mistura (blend crossover) beta pertence a
	 * uma distribuição uniforme entre -alpha e 1+alpha o alpha extende o intervalo
	 * para ambos os lados
	 * 
	 * # TODO variáveis de tamanho diferente lançam erro
	 */
	public Chromosome blxAlpha(Chromosome c1, Chromosome c2) {
		double alpha = 0.4;
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
