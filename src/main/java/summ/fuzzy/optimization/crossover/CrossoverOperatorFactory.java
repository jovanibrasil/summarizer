package summ.fuzzy.optimization.crossover;

public class CrossoverOperatorFactory {

	public enum CrossoverOperatorType {
		SIMPLE_MEAN, GEOMETRIC_MEAN, N_POINT, BLX
	}
	
	public static CrossoverOperator getCrossoverOperator(CrossoverOperatorType crossoverOperatorType) {
		switch (crossoverOperatorType) {
			case SIMPLE_MEAN:
				return new SimpleMeanCrossover();
			case GEOMETRIC_MEAN:
				return new GeometricMeanCrossover();
			case N_POINT:
				return new NpointInterleaveCrossover(2);
			case BLX:
				return new BlxCrossover();
			default:
				return null;
		}
	}
	
}
