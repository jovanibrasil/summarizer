package summ.fuzzy.optimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.FuzzySystem;
import summ.fuzzy.optimization.evaluation.ErrorFunctionSummarization;
import summ.fuzzy.optimization.settings.OptimizationSettings;
import summ.fuzzy.optimization.settings.OptimizationSettingsUtils;
import summ.model.Text;
import summ.nlp.evaluation.EvaluationResult;
import summ.nlp.evaluation.EvaluationTypes;
import summ.settings.GlobalSettings;
import summ.summarizer.Summarizer;
import summ.utils.Charts;
import summ.utils.ExportHTML;
import summ.utils.FileUtils;
import summ.utils.Utils;

public class Optimization {
	
	private static final Logger log = LogManager.getLogger(Optimization.class);

	public Optimization() {}
	
	public EvaluationResult evaluateCorpus(Summarizer summarizer, FuzzySystem fuzzySystem, List<String> metrics, List<Text> texts, OptimizationSettings optimizationSettings) {
		
		EvaluationResult eval = new EvaluationResult();
		metrics.forEach(m -> {eval.setMetric(m, 0);});
	
		for (Text text : texts) {
        	Text generatedSummary = summarizer.summarizeText(text, fuzzySystem, optimizationSettings.VAR_NAMES);
        	EvaluationResult result = summarizer.evaluateSummary(optimizationSettings.EVALUATION_METHOD, generatedSummary, text.getReferenceSummary());
        	metrics.forEach(m -> {eval.setMetric(m, eval.getMetricValue(m) + result.getMetricValue(m));});
        }
    	metrics.forEach(m -> {eval.setMetric(m, eval.getMetricValue(m) / texts.size());});
		
		return eval;
	}
	
	public Chromosome runGeneticOptimization(OptimizationGenetic geneticOptimization, List<Object> logs) {
		log.info("Optimization Start time: " + new Date());
		long startTime = System.nanoTime();
		Chromosome bestSolution = geneticOptimization.run();
		long endTime = System.nanoTime();
		log.info("End time: " + new Date());
		long timeElapsed = (endTime - startTime) / 1000000;
		log.info("Execution time in milliseconds : " + timeElapsed);
		logs.add("Execution time in milliseconds : " + timeElapsed);
		return bestSolution;
	}
	
	public List<String> getFileNames(List<Text> texts){
		return texts.stream().map(t -> { return t.getName(); }).collect(Collectors.toList());			
	}
	
	public void run(GlobalSettings globalSettings) {
		
		List<Object> objs = new ArrayList<Object>();
		
		List<String> metrics = Arrays.asList(EvaluationTypes.PRECISION.name(), 
				EvaluationTypes.RECALL.name(), EvaluationTypes.FMEASURE.name());
		
		log.info("Initializing an summarization tool ...");
		Summarizer summarizer = new Summarizer(globalSettings);	
		
		// Load all corpus
		int	corpusSize = 0;//FileUtils.countFiles(globalSettings.CORPUS_PATH);
		List<Text> corpus = FileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.AUTO_SUMMARIES_PATH, corpusSize);	
		// Pre-process and compute sentence features
		summarizer.prepareTextList(corpus);
		
		log.info("Load Evaluation files ...");
		List<Text> texts = FileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.AUTO_SUMMARIES_PATH, globalSettings.EVALUATION_LEN);	
		summarizer.prepareTextList(texts); // pre-process and calculate features
		
		log.info("Initializing genetic optimization configurations ...");
		
		
		for (String optFileName : globalSettings.OPTIMIZATION_FILES) { // for each configuration file, execute the optimization
		
			globalSettings.OPTIMIZATION_PROPERTIES_PATH += optFileName;
			OptimizationSettings optSettings = new OptimizationSettings();
			
			OptimizationSettingsUtils.loadOptimizationProps(globalSettings.OPTIMIZATION_PROPERTIES_PATH, optSettings);

			globalSettings.OUTPUT_PATH = "results/opt_" + optSettings.OPTIMIZATION_NAME + Utils.generateStringFormattedData();
		    FileUtils.createDir(globalSettings.OUTPUT_PATH);
		    
		    log.info("Initializing texts ...");		
		    
			FuzzySystem fs = new FuzzySystem(optSettings.FUZZY_SYSTEM_PATH);
			fs.showFuzzySystem();
			fs.setOutputVariable(optSettings.VAR_NAMES.get(optSettings.VAR_NAMES.size()-1));
			
		    ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(summarizer, texts, 
		    		optSettings.EVALUATION_METHOD, optSettings.VAR_NAMES); 
		    
			OptimizationGenetic geneticOptimization = new OptimizationGenetic(errorFunction, 
					optSettings.CROSSOVER_OPERATOR, optSettings.MUTATION_OPERATOR, 
					optSettings.CROSSOVER_PROBABILITY, optSettings.MUTATION_PROBABILITY, 
					optSettings.GENE_MUTATION_PERCENTUAL, optSettings.ELITISM, optSettings.POPULATION_SIZE, 
				optSettings.VAR_NAMES, fs ,optSettings.MAX_ITERATIONS);

			log.info(optSettings);
			
			log.info("Initial corpus evalution");
			EvaluationResult initialCorpusEvaluation = evaluateCorpus(summarizer, fs, metrics, corpus, optSettings);
			initialCorpusEvaluation.setEvalName("Initial Corpus Evaluation");
			
			// run the genetic optimization
			Chromosome bestSolution = this.runGeneticOptimization(geneticOptimization, objs);
			fs.setCoefficients(bestSolution.getGenes());
			
			log.info("Final corpus evalution");
			EvaluationResult finalCorpusEvaluation = evaluateCorpus(summarizer, fs, metrics, corpus, optSettings);
			finalCorpusEvaluation.setEvalName("Final Corpus Evaluation");
			
			// Create results directory
			String textsResultsPath = globalSettings.OUTPUT_PATH + "/texts-results";
			FileUtils.createDir(textsResultsPath);

			// Get optimization best fitness for each iteration. Save this series as a chart. 
			List<Double> bestFitnessSerie = geneticOptimization.bestFitnessSerie;
			Charts charts = new Charts(bestFitnessSerie, geneticOptimization.averageFitnessSerie, 
					optSettings.MAX_ITERATIONS, "genetic-optimization_" + optFileName);
			charts.saveChart(globalSettings.OUTPUT_PATH + "/chart.png");
			
			// Save pre-processing and feature computation results
			texts.forEach(text -> {
				ExportHTML.exportSentecesAndFeatures(text, summarizer.summarizationSettings.OUTPUT_EXPORT_VARIABLES, textsResultsPath, "full-text");
			});
			
			objs.add(Arrays.asList(bestFitnessSerie, geneticOptimization.worstFitnessSerie,  geneticOptimization.averageFitnessSerie, 
					globalSettings, optSettings, getFileNames(texts), initialCorpusEvaluation, finalCorpusEvaluation));
			FileUtils.saveListOfObjects(objs, globalSettings.OUTPUT_PATH + "/result_" + optSettings.OPTIMIZATION_NAME + ".txt");
		
			// save optimized result
			fs.saveFuzzySystem(globalSettings.OUTPUT_PATH + "/opt_" + optSettings.OPTIMIZATION_NAME + ".fcl");
			
		}
		
	}
	
	
}
