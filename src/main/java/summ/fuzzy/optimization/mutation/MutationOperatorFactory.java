package summ.fuzzy.optimization.mutation;

public class MutationOperatorFactory {

	public enum MutationOperatorType {
		CREEP, GAUSSIAN, LIMIT, UNIFORM, NON_UNIFORM
	}
	
	public static MutationOperator getMutationOperator(MutationOperatorType mutationOperatorType) {
		switch (mutationOperatorType) {
			case UNIFORM:
				return new UniformMutation();
			case NON_UNIFORM:
				return new NonUniformMutation();
			case GAUSSIAN:
				return new GaussianMutation();
			case LIMIT:
				return new LimitMutation();
			default:
				return null;
		}
	}
	
}
