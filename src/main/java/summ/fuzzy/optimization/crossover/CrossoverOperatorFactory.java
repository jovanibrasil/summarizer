package summ.fuzzy.optimization.crossover;

import summ.fuzzy.optimization.functions.Function;

public class CrossoverOperatorFactory {

	public enum CrossoverOperatorType {
		SIMPLE_MEAN, GEOMETRIC_MEAN, N_POINT, BLX
	}
	
	public static CrossoverOperator getCrossoverOperator(CrossoverOperatorType crossoverOperatorType, Function function) {
		switch (crossoverOperatorType) {
			case SIMPLE_MEAN:
				return new SimpleMeanCrossover(function);
			case GEOMETRIC_MEAN:
				return new GeometricMeanCrossover(function);
			case N_POINT:
				return new NpointInterleaveCrossover(2, function);
			case BLX:
				return new BlxCrossover(function);
			default:
				return null;
		}
	}

}
