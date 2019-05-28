package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.functions.Function;

public class MutationOperatorFactory {

	public enum MutationOperatorType {
		GAUSSIAN, LIMIT, UNIFORM, NON_UNIFORM, NORMAL_CREEP, DISTURB_CREEP
	}
	
	public static MutationOperator getMutationOperator(MutationOperatorType mutationOperatorType, Function function) {
		switch (mutationOperatorType) {
			case UNIFORM:
				return new UniformMutation(function);
			case NON_UNIFORM:
				return new NonUniformMutation(function);
			case GAUSSIAN:
				return new GaussianMutation(function);
			case LIMIT:
				return new LimitMutation(function);
			case NORMAL_CREEP:
				return new CreepMutation(MutationOperatorType.NORMAL_CREEP, function);
			case DISTURB_CREEP:
				return new CreepMutation(MutationOperatorType.DISTURB_CREEP, function);
			default:
				return null;
		}
	}
	
}
