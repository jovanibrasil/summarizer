package summ.fuzzy.optimization.mutation;

public class MutationOperatorFactory {

	public enum MutationOperatorType {
		GAUSSIAN, LIMIT, UNIFORM, NON_UNIFORM, NORMAL_CREEP, DISTURB_CREEP
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
			case NORMAL_CREEP:
				return new CreepMutation(MutationOperatorType.NORMAL_CREEP);
			case DISTURB_CREEP:
				return new CreepMutation(MutationOperatorType.DISTURB_CREEP);
			default:
				return null;
		}
	}
	
}
