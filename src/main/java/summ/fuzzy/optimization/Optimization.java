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

	List<String> metrics = Arrays.asList(EvaluationTypes.PRECISION.name(), 
			EvaluationTypes.RECALL.name(), EvaluationTypes.FMEASURE.name());
	
	private Summarizer summarizer;
	private GlobalSettings globalSettings;
	
	private List<Text> trainingTexts;
	private List<Text> testTests;
	
	private int docEvalMaxIterations = 4;
	private int corpusSize = 100; //countFiles(textsDir); 
	
	public Optimization(GlobalSettings globalSettings) {
				
		log.info("Initializing an summarization tool ...");
		this.summarizer = new Summarizer(globalSettings);	
		
		// TODO load files using a list of file names
		
		List<List<Text>> data = FileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.MANUAL_SUMMARIES_PATH, corpusSize, globalSettings.TRAINING_TEXTS_PERCENTUAL);	
		this.trainingTexts = data.get(0);
		this.testTests = data.get(1);
		
		// set x
		//List<String> trainingTextsNames = Arrays.asList("in96ju18-a", "in96ju10-a");
		//List<String> testTextsNames = Arrays.asList("op94ab06-a", "ce94jl11-b");
				
		
		// set 0
		//List<String> trainingTextsNames = Arrays.asList("ce94ja25-b", "op94ag21-a", "in96jl02-a", "op94ag07-c", "in96ab09-b", "ce94jl31-b", "mu94ag09-a");
		//List<String> testTextsNames = Arrays.asList("ce94ja8-a", "in96ab19-a", "ce94jl11-b");
		
		// set 1
		//List<String> trainingTextsNames = Arrays.asList("po96fe09-a", "mu94de05-a", "op94ag14-b", "po96jl01-b", "po96ab19-a", "ce94ja25-b", "in96ab26-b");
		//List<String> testTextsNames = Arrays.asList("op94ag21-a", "po96fe13-a", "op94ab18-a");
		
		// set 2 opt__Fri_May_31_03_40_02_BRT_2019
		//List<String> trainingTextsNames = Arrays.asList("in96ju18-a", "in96ju10-a", "mu94ab03-a", "in96fe08-a", "op94ab21-a", "in96fe29-a", "po96fe14-c");
		//List<String> testTextsNames = Arrays.asList("op94ab06-a", "ce94jl11-b", "mu94de04-b");
		
		// set 3
		//List<String> trainingTextsNames = Arrays.asList("po96fe09-b", "op94ab01-b", "op94ab10-a", "mu94ab03-a", "ce94ja21-d", "op94ab04-a", "po96fe26-a");
		//this.trainingTexts = FileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.MANUAL_SUMMARIES_PATH, trainingTextsNames);
		//List<String> testTextsNames = Arrays.asList("po96fe14-b", "op94ab26-a", "po96fe28-a");
		//this.testTests = FileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.MANUAL_SUMMARIES_PATH, testTextsNames);  
		
		this.summarizer.prepareTextList(trainingTexts); // Pre-process and compute sentence features
		this.summarizer.prepareTextList(testTests); // pre-process and calculate features
		
		this.globalSettings = globalSettings;
		
	}
	
	public EvaluationResult evaluateCorpus(Summarizer summarizer, FuzzySystem fuzzySystem,  
			List<Text> texts, OptimizationSettings optimizationSettings, String evaluationName) {
		
		EvaluationResult eval = new EvaluationResult();
		metrics.forEach(m -> {eval.setMetric(m, 0);});
		for (Text text : texts) {
        	Text generatedSummary = summarizer.summarizeText(text, fuzzySystem, optimizationSettings.VAR_NAMES);
        	EvaluationResult result = summarizer.evaluateSummary(optimizationSettings.EVALUATION_METHOD, generatedSummary, text.getReferenceSummary());
        	metrics.forEach(m -> {eval.setMetric(m, eval.getMetricValue(m) + result.getMetricValue(m));});
        }
    	metrics.forEach(m -> {eval.setMetric(m, eval.getMetricValue(m) / texts.size());});
		
    	eval.setEvalName(evaluationName);
		log.info(eval);
		
		return eval;
	}
	
	public Chromosome runGeneticOptimization(GeneticOptimization geneticOptimization, List<Object> logs) {
		log.info("Optimization Start time: " + new Date());
		long startTime = System.nanoTime();
		Chromosome bestSolution = geneticOptimization.run();
		log.info("End time: " + new Date());
		long timeElapsed = (System.nanoTime() - startTime) / 1000000;
		log.info("Execution time in milliseconds : " + timeElapsed);
		logs.add("Execution time in milliseconds : " + timeElapsed);
		return bestSolution;
	}
	
	public List<String> getFileNames(List<Text> texts){
		return texts.stream().map(t -> { return t.getName(); }).collect(Collectors.toList());			
	}
	
	public List<Double> run(int iteration) {
		
		List<Double> optimizationResults = new ArrayList<Double>();
		
		log.info("Initializing genetic optimization configurations ...");
		
		String outputPath = globalSettings.OUTPUT_PATH + "/iteration" + iteration;
		String optSettingsPath = globalSettings.OPTIMIZATION_PROPERTIES_PATH;
		
		for (String optFileName : globalSettings.OPTIMIZATION_FILES) { // for each configuration file, execute the optimization
			
			globalSettings.OPTIMIZATION_PROPERTIES_PATH = optSettingsPath + optFileName;
			OptimizationSettings optSettings = new OptimizationSettings(globalSettings.OPTIMIZATION_PROPERTIES_PATH);
			log.debug(optSettings);
			
			// create iteration results directory
			globalSettings.OUTPUT_PATH = outputPath + "/opt_" + optSettings.OPTIMIZATION_NAME + Utils.generateStringFormattedData();
		    FileUtils.createDir(globalSettings.OUTPUT_PATH);
		    
		    // Create texts results directory
			String textsResultsPath = globalSettings.OUTPUT_PATH + "/texts-results";
			FileUtils.createDir(textsResultsPath);
		    
		    FuzzySystem fs = new FuzzySystem(optSettings.FUZZY_SYSTEM_PATH);
		    fs.setOutputVariable("informativity");
		    
		    ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(summarizer, trainingTexts, 
		    		optSettings.EVALUATION_METHOD, optSettings.VAR_NAMES); 
		    
			//List<Double> iterationResults = new ArrayList<>();
			double averageFitness = 0.0;
			int docEvalIterationsCounter = 0;
			List<List<Double>> series = new ArrayList<List<Double>>();
			while(docEvalIterationsCounter < docEvalMaxIterations) { // each optimization is executed a specified number of times
				
				log.info("Iteration: " + (docEvalIterationsCounter+1) + "/" + docEvalMaxIterations);
			
				GeneticOptimization geneticOptimization = new GeneticOptimization(errorFunction, optSettings, fs);
				List<Object> objs = new ArrayList<Object>();
				
				// run the genetic optimization
				Chromosome bestSolution = this.runGeneticOptimization(geneticOptimization, objs);
				fs.setCoefficients(bestSolution.getGenes());
				
				// run the final corpus evaluation
				EvaluationResult finalEvaluation = evaluateCorpus(summarizer, fs, testTests, optSettings, "Final Corpus Evaluation");
				averageFitness += finalEvaluation.getMetricValue("RECALL");  // TODO automate the metric name
				
				// Get optimization best fitness for each iteration. Save this series as a chart. 
				List<Double> bestFitnessSerie = geneticOptimization.bestFitnessSerie;
				series.add(bestFitnessSerie);
				
				// Save pre-processing and feature computation results
				trainingTexts.forEach(text -> {
					ExportHTML.exportSentecesAndFeatures(text, summarizer.summarizationSettings.OUTPUT_EXPORT_VARIABLES, textsResultsPath, "full-text");
				});
				
				objs.add(Arrays.asList(bestFitnessSerie, geneticOptimization.worstFitnessSerie, 
						globalSettings, optSettings, "training files: " + getFileNames(trainingTexts), 
						"evaluation files: " + getFileNames(testTests), finalEvaluation));
				FileUtils.saveListOfObjects(objs, globalSettings.OUTPUT_PATH + "/result_" + docEvalIterationsCounter + "_" + optSettings.OPTIMIZATION_NAME + ".txt");
				// save optimized result
				fs.showFuzzySystem();
				fs.saveFuzzySystem(globalSettings.OUTPUT_PATH + "/opt_"+ docEvalIterationsCounter +"_" + optSettings.OPTIMIZATION_NAME + ".fcl");
				
				docEvalIterationsCounter++;
			}
			
			Charts charts = new Charts(series, optSettings.MAX_ITERATIONS, "genetic-optimization_" + optFileName);
			charts.saveChart(globalSettings.OUTPUT_PATH + "/chart.png");
			double averageScore = averageFitness / docEvalMaxIterations;
			optimizationResults.add(averageScore); // 
			FileUtils.saveListOfObjects(Arrays.asList("Average score: " + averageScore), 
					globalSettings.OUTPUT_PATH + "/result_" + optSettings.OPTIMIZATION_NAME + ".txt");
		}
		log.info("Bye!");
		return optimizationResults;
	}
	
	
}
