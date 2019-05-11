package summ.fuzzy.optimization;

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

	private Summarizer summarizer;
	private OptimizationGenetic geneticOptimization;
	private GlobalSettings summarizerSettings;
	private OptimizationSettings optimizationSettings;
	private List<Text> texts;
	private FuzzySystem fs;
	
	public Optimization(GlobalSettings summarizerSettings) {
		
		log.info("Initializing an summarization tool ...");
		this.summarizer = new Summarizer(summarizerSettings);	
		
		log.info("Initializing genetic optimization ...");
		this.optimizationSettings = new OptimizationSettings();
		OptimizationSettingsUtils.loadOptimizationProps(summarizerSettings.OPTIMIZATION_PROPERTIES_PATH, optimizationSettings);

		String fileName = "results/opt_" + this.optimizationSettings.OPTIMIZATION_NAME + Utils.generateStringFormattedData();
	    FileUtils.createDir(fileName);
	    summarizerSettings.OUTPUT_PATH = fileName;
	    this.summarizerSettings = summarizerSettings;
		
	    log.info("Initializing texts ...");		
	    
	    this.texts = FileUtils.loadTexts(optimizationSettings.FULL_TEXTS_PATH, optimizationSettings.AUTO_SUMMARIES_PATH, optimizationSettings.EVALUATION_LEN);	
		this.summarizer.prepareTextList(this.texts); // pre-process and calculate features
		
		this.fs = new FuzzySystem(optimizationSettings.FUZZY_SYSTEM_PATH);
		fs.showFuzzySystem();
		this.fs.setOutputVariable(optimizationSettings.VAR_NAMES.get(optimizationSettings.VAR_NAMES.size()-1));
		
	    ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(this.fs, this.summarizer, this.texts, 
	    		optimizationSettings.EVALUATION_METHOD, optimizationSettings.VAR_NAMES); 
	    
		this.geneticOptimization = new OptimizationGenetic(this.fs.getRuleBlock(), errorFunction, optimizationSettings.CROSSOVER_OPERATOR, 
			optimizationSettings.MUTATION_OPERATOR, optimizationSettings.CROSSOVER_PROBABILITY, optimizationSettings.MUTATION_PROBABILITY, 
			optimizationSettings.GENE_MUTATION_PROBABILITY, optimizationSettings.ELITISM, optimizationSettings.POPULATION_SIZE, optimizationSettings.VAR_NAMES);

		geneticOptimization.setMaxIterations(optimizationSettings.MAX_ITERATIONS);
		geneticOptimization.setVerbose(false);
		
		log.info(optimizationSettings);
		
	}
	
	public EvaluationResult evaluateCorpus(List<String> metrics, List<Text> corpus) {
		
		EvaluationResult eval = new EvaluationResult();
		metrics.forEach(m -> {eval.setMetric(m, 0);});
	
		for (Text text : corpus) {
        	Text generatedSummary = this.summarizer.summarizeText(text, this.fs, optimizationSettings.VAR_NAMES);
        	EvaluationResult result = this.summarizer.evaluateSummary(optimizationSettings.EVALUATION_METHOD, generatedSummary, text.getReferenceSummary());
        	metrics.forEach(m -> {eval.setMetric(m, eval.getMetricValue(m) + result.getMetricValue(m));});
        }
    	metrics.forEach(m -> {eval.setMetric(m, eval.getMetricValue(m) / corpus.size());});
		
		return eval;
	}
		
	public void run() {
		
		// Load files
		int	evaluationSize = FileUtils.countFiles(optimizationSettings.FULL_TEXTS_PATH);
		List<Text> corpus = FileUtils.loadTexts(optimizationSettings.FULL_TEXTS_PATH, optimizationSettings.AUTO_SUMMARIES_PATH, evaluationSize);	
		// Pre-process and compute sentence features
		this.summarizer.prepareTextList(corpus);
		
		List<String> metrics = Arrays.asList(EvaluationTypes.PRECISION.name(), 
				EvaluationTypes.RECALL.name(), EvaluationTypes.FMEASURE.name());
		
		log.info("Initial corpus evalution");
		EvaluationResult initialCorpusEvaluation = evaluateCorpus(metrics, corpus);
		initialCorpusEvaluation.setEvalName("Initial Corpus Evaluation");
		
		log.info("Optimization Start time: " + new Date());
		long startTime = System.nanoTime();
		this.geneticOptimization.optimize();
		long endTime = System.nanoTime();
		log.info("End time: " + new Date());
		long timeElapsed = (endTime - startTime) / 1000000;
		log.info("Execution time in milliseconds : " + timeElapsed);

		log.info("Final corpus evalution");
		EvaluationResult finalCorpusEvaluation = evaluateCorpus(metrics, corpus);
		finalCorpusEvaluation.setEvalName("Final Corpus Evaluation");
		
		// Create results directory
		String textsResultsPath = this.summarizerSettings.OUTPUT_PATH + "/texts-results";
		FileUtils.createDir(textsResultsPath);

		// Get optimization best fitness for each iteration. Save this serie as a chart. 
		List<Double> dataSerie = this.geneticOptimization.getDataSerie();
		Charts charts = new Charts(dataSerie, "genetic-optimization");
		charts.saveChart(summarizerSettings.OUTPUT_PATH + "/chart.png");
		
		// Get list of file names
		List<String> fileNames = this.texts.stream().map(t -> { return t.getName(); }).collect(Collectors.toList());
		
		// Save pre-processing and feature computation results
		this.texts.forEach(text -> {
			ExportHTML.exportSentecesAndFeatures(text, summarizer.summarizationSettings.OUTPUT_EXPORT_VARIABLES, textsResultsPath);
		});
		
		List<Object> objs = Arrays.asList("Execution time in milliseconds : " + timeElapsed, dataSerie, 
				this.summarizerSettings, this.optimizationSettings, fileNames, initialCorpusEvaluation, finalCorpusEvaluation);
		
		FileUtils.saveListOfObjects(objs, this.summarizerSettings.OUTPUT_PATH + "/result_" + this.optimizationSettings.OPTIMIZATION_NAME + ".txt");
	
		// save optimized result
		fs.saveFuzzySystem(this.summarizerSettings.OUTPUT_PATH + "/opt_" + this.optimizationSettings.OPTIMIZATION_NAME + ".fcl");
		
	}
	
}
