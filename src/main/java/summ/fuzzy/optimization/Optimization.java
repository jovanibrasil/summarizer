package summ.fuzzy.optimization;

import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.FuzzySystem;
import summ.fuzzy.optimization.evaluation.ErrorFunctionSummarization;
import summ.fuzzy.optimization.settings.OptimizationSettings;
import summ.fuzzy.optimization.settings.OptimizationSettingsUtils;
import summ.model.Text;
import summ.settings.SummarizerSettings;
import summ.summarizer.Summarizer;
import summ.utils.Charts;
import summ.utils.Utils;


public class Optimization {
	
	private static final Logger log = LogManager.getLogger(Optimization.class);

	private OptimizationGenetic geneticOptimization;
	private SummarizerSettings summarizerSettings;
	
	public Optimization(SummarizerSettings summarizerSettings) {
		
		String fileName = "results/opt_" + (new Date().toString()).replace("-", "_").replace(" ", "_").replace(":", "_");
	    Utils.createDir(fileName);
	    summarizerSettings.OUTPUT_PATH = fileName;
	    this.summarizerSettings = summarizerSettings;
		
	    
		log.info("Initializing an summarization tool ...");
		Summarizer summ = new Summarizer(summarizerSettings);		
		log.info("Initializing genetic optimization ...");
		
		OptimizationSettings settings = new OptimizationSettings();
		OptimizationSettingsUtils.loadOptimizationProps(summarizerSettings.OPTIMIZATION_PROPERTIES_PATH, settings);

	    log.info("Initializing texts ...");		
	    
	    List<Text> texts = Utils.loadTexts(settings.FULL_TEXTS_PATH, settings.AUTO_SUMMARIES_PATH, settings.EVALUATION_LEN);	
		summ.prepareTextList(texts); // pre-process and calculate features
		
		FuzzySystem fs = new FuzzySystem(settings.FUZZY_SYSTEM_PATH);
		fs.setOutputVariable(settings.VAR_NAMES.get(settings.VAR_NAMES.size()-1));
		
	    ErrorFunctionSummarization errorFunction = new ErrorFunctionSummarization(fs, summ, texts, settings.EVALUATION_METHOD, settings.VAR_NAMES); 
	    
		FIS fis = FIS.load(settings.FUZZY_SYSTEM_PATH);
		RuleBlock ruleBlock = fis.getFunctionBlock(null).getFuzzyRuleBlock(null);
		
		this.geneticOptimization = new OptimizationGenetic(ruleBlock, errorFunction, settings.CROSSOVER_OPERATOR, 
			settings.MUTATION_OPERATOR, settings.CROSSOVER_PROBABILITY, settings.MUTATION_PROBABILITY, settings.ELITISM, 
			settings.POPULATION_SIZE, settings.VAR_NAMES);

		geneticOptimization.setMaxIterations(settings.MAX_ITERATIONS);
		geneticOptimization.setVerbose(false);
		
		log.info(settings);
		
	}
	
	public void showResult() {
		// save optimized result
		//		System.out.println(ruleBlock.toStringFcl());
		//	    Gpr.toFile("flc/fb2015_optimized.flc", ruleBlock.getFunctionBlock().toString() + ruleBlock.toString() );
		//		
	    // functionBlock.reset();
	    // JFuzzyChart.get().chart(functionBlock);
	}
	
	public void run() {
		this.geneticOptimization.optimize(); 
		List<Double> dataSerie = this.geneticOptimization.getDataSerie();
		SwingUtilities.invokeLater(() -> {
			Charts charts = new Charts(dataSerie, "genetic-optimization");
			charts.setVisible(true);
			charts.saveChart(summarizerSettings.OUTPUT_PATH + "/chart.png");
		});
		
	}
	
}
