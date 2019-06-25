package summ.fuzzy.optimization.settings;

import java.util.List;

import summ.fuzzy.optimization.crossover.CrossoverOperator;
import summ.fuzzy.optimization.mutation.MutationOperator;
import summ.nlp.evaluation.EvaluationMethod;

public class OptimizationSettings {

	public int MAX_ITERATIONS = 20;
	public double MUTATION_PROBABILITY = 0.1;
	public double CROSSOVER_PROBABILITY = 0.6;
	public double GENE_MUTATION_PERCENTUAL = 0.7;
	public boolean ELITISM = true;
	public int POPULATION_SIZE = 10;
	public int EVALUATION_LEN = 10;
	public List<String> VAR_NAMES;
	public EvaluationMethod EVALUATION_METHOD;
	public MutationOperator MUTATION_OPERATOR;
	public CrossoverOperator CROSSOVER_OPERATOR;
	public boolean SEEDING;
	public String FUZZY_SYSTEM_PATH;
	public String FULL_TEXTS_PATH;
	public String AUTO_SUMMARIES_PATH;
	public String OPTIMIZATION_NAME;
	
	public OptimizationSettings(String optFilePath) {
		OptimizationSettingsUtils.loadOptimizationProps(optFilePath, this);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Optimization settings:")
			.append("\n\tError metric: ").append(this.EVALUATION_METHOD)
			.append("\n\tCrossover operator: ").append(this.CROSSOVER_OPERATOR)		
			.append("\n\tMutation operator: ").append(this.MUTATION_OPERATOR)
			.append("\n\tCrossover probability: ").append(this.CROSSOVER_PROBABILITY)
			.append("\n\tMutation probability: ").append(this.MUTATION_PROBABILITY)
			.append("\n\tElitism: ").append(this.ELITISM)
			.append("\n\tSeeding: ").append(this.SEEDING)
			.append("\n\tPopulation size: ").append(this.POPULATION_SIZE)
			.append("\n\tOptimization variables: ").append(this.VAR_NAMES)
			.append("\n\tMax iterations: ").append(this.MAX_ITERATIONS)
			.append("\n\tEvaluation len: ").append(this.EVALUATION_LEN)
			.append("\n\tFuzzy system path: ").append(this.FUZZY_SYSTEM_PATH)
			.append("\n\tFull texts path: ").append(this.FULL_TEXTS_PATH)
			.append("\n\tAuto summaries path: ").append(this.AUTO_SUMMARIES_PATH);
		
		return sb.toString();
	}
	
}
