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
	
	private List<List<Text>> trainingTextsFiles = new ArrayList<List<Text>>();
	private List<List<Text>> testTextsFiles = new ArrayList<List<Text>>();
	
	private int docEvalMaxIterations = 2;
	private int corpusSize = 80; //countFiles(textsDir); 
	
	public Optimization(GlobalSettings globalSettings) {
				
		log.info("Initializing an summarization tool ...");
		this.summarizer = new Summarizer(globalSettings);	
		
		if(globalSettings.optimizationEvaluationFiles == null && globalSettings.optimizationFiles == null) {
			// TODO a quantidade de conjuntos deve ser passada por parâmetro
			Arrays.asList(0, 1, 3).forEach(x -> {
				List<List<Text>> data = FileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.MANUAL_SUMMARIES_PATH, corpusSize, globalSettings.TRAINING_TEXTS_PERCENTUAL);	
				
				List<Text> trainingTexts = data.get(0);
				List<Text> testTests = data.get(1);		
				
				this.summarizer.prepareTextList(trainingTexts);
				this.summarizer.prepareTextList(testTests);
				
				this.trainingTextsFiles.add(trainingTexts);
				this.testTextsFiles.add(testTests);
				
			});
			
			System.out.println("Training files");
			trainingTextsFiles.forEach(x-> {
				System.out.println(getFileNames(x));
			});
			System.out.println("Evaluation files");
			testTextsFiles.forEach(x-> {
				System.out.println(getFileNames(x));
			});
		
		}else {
			
			for (String[] fileNames : globalSettings.optimizationFiles) {
				List<Text> texts = FileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.MANUAL_SUMMARIES_PATH, fileNames);
				this.summarizer.prepareTextList(texts);
				this.trainingTextsFiles.add(texts);
			}
			
			for (String[] fileNames : globalSettings.optimizationEvaluationFiles) {
				List<Text> texts = FileUtils.loadTexts(globalSettings.CORPUS_PATH, globalSettings.MANUAL_SUMMARIES_PATH, fileNames);
				this.summarizer.prepareTextList(texts);
				this.testTextsFiles.add(texts);
			}
			
		}
		
		this.globalSettings = globalSettings;
		
	}
	
	public EvaluationResult evaluateCorpus(Summarizer summarizer, FuzzySystem fuzzySystem,  
			List<Text> texts, OptimizationSettings optimizationSettings, String evaluationName) {
		
		EvaluationResult eval = new EvaluationResult();
		metrics.forEach(m -> {eval.setMetric(m, 0);});
		for (Text text : texts) {
        	Text generatedSummary = summarizer.summarizeText(text, fuzzySystem, optimizationSettings.VAR_NAMES, false);
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
		
		List<Text> trainingTexts = trainingTextsFiles.get(iteration);
		List<Text> testTests = testTextsFiles.get(iteration);
		
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
