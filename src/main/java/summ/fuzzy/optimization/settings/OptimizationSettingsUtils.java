package summ.fuzzy.optimization.settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;

import summ.fuzzy.optimization.crossover.CrossoverOperatorFactory;
import summ.fuzzy.optimization.crossover.CrossoverOperatorFactory.CrossoverOperatorType;
import summ.fuzzy.optimization.functions.GbellFunction;
import summ.fuzzy.optimization.functions.Function;
import summ.fuzzy.optimization.mutation.MutationOperatorFactory;
import summ.fuzzy.optimization.mutation.MutationOperatorFactory.MutationOperatorType;
import summ.nlp.evaluation.EvaluationMethodFactory;
import summ.nlp.evaluation.EvaluationTypes;
import summ.utils.CustomFileUtils;

public class OptimizationSettingsUtils {
	
	static Logger log = Logger.getLogger(OptimizationSettingsUtils.class);

	public static void loadOptimizationProps(String propertyFilePath, OptimizationSettings rs) {
		
		InputStream stream = CustomFileUtils.loadProps(propertyFilePath);
		Properties properties = new Properties();

		try {
			properties.load(stream);

			String stringList[] = propertyFilePath.split("/");
			rs.OPTIMIZATION_NAME = stringList[stringList.length-1].replace(".properties", "").trim(); 
			
			String val = properties.getProperty("max_iterations", "20");
			rs.MAX_ITERATIONS = Integer.parseInt(val);

			val = properties.getProperty("population_size", "10");
			rs.POPULATION_SIZE = Integer.parseInt(val);
			
			val = properties.getProperty("mutation_probability", "0.1");
			rs.MUTATION_PROBABILITY = Double.parseDouble(val);
			
			val = properties.getProperty("crossover_probability", "0.1");
			rs.CROSSOVER_PROBABILITY = Double.parseDouble(val);
			
			val = properties.getProperty("elitism", "true");
			rs.ELITISM = Boolean.parseBoolean(val);
			
			val = properties.getProperty("seeding", "false");
			rs.SEEDING = Boolean.parseBoolean(val);
			
			val = properties.getProperty("var_names", "").replace(" ", "");
			if(val.isEmpty()) {
				rs.VAR_NAMES = new ArrayList<>();
			} else {
				rs.VAR_NAMES = Arrays.asList(val.split(","));
			}
			
			val = properties.getProperty("gene_mutation_percentual", "0.7");
			rs.GENE_MUTATION_PERCENTUAL = Double.parseDouble(val);
			
			val = properties.getProperty("evaluation_len", "10");
			rs.EVALUATION_LEN = Integer.parseInt(val);
			
			Function function = new GbellFunction();
			val = properties.getProperty("mutation_operator", "");
			rs.MUTATION_OPERATOR = MutationOperatorFactory
					.getMutationOperator(MutationOperatorType.valueOf(val), function);
			
			val = properties.getProperty("crossover_operator", "");
			rs.CROSSOVER_OPERATOR = CrossoverOperatorFactory
					.getCrossoverOperator(CrossoverOperatorType.valueOf(val), function);
			
			val = properties.getProperty("evaluation_method", "");
			rs.EVALUATION_METHOD = EvaluationMethodFactory
					.getEvaluationMethod(EvaluationTypes.valueOf(val));
			
			rs.FULL_TEXTS_PATH = properties.getProperty("full_texts_path", "").trim();
			rs.AUTO_SUMMARIES_PATH = properties.getProperty("auto_summaries_path", "").trim();
			rs.FUZZY_SYSTEM_PATH  = properties.getProperty("fuzzy_system_path", "").trim();
			
		} catch (IOException e) {
			log.warn("Problem with settings model file." + e.getMessage());
			log.warn("If you want to use the optimization tool, please fix this issue first before proceeding.");
		}

	}
}
