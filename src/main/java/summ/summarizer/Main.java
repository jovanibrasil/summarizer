package summ.summarizer;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.optimization.Optimization;
import summ.gui.SummGUI;
import summ.settings.GlobalSettings;
import summ.settings.GlobalSettings.ExecutionType;
import summ.utils.ExportCSV;
import summ.utils.Utils;

public class Main {

	private static final Logger log = LogManager.getLogger(Main.class);
	
	public static void invokeGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SummGUI window = new SummGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		
		//invokeGUI();
		GlobalSettings summarizerSettings = new GlobalSettings();
		
		if(summarizerSettings.EXECUTION_TYPE.equals(ExecutionType.OPTIMIZATION)) {
			List<List<Double>> results = new ArrayList<>();
			String outputFilePath = "results/opt_" + Utils.generateStringFormattedData();
			
			Arrays.asList(0).forEach(i -> { //parallelStream().forEach(i -> {
				GlobalSettings settings = new GlobalSettings();
				settings.OUTPUT_PATH = outputFilePath;
				Optimization optmization = new Optimization(settings);
				List<Double> result = optmization.run(i); // each iteration has the same configuration but different data
			    results.add(result);
			});
			
			ExportCSV.exportOptimizationResults(results, summarizerSettings.OPTIMIZATION_FILES, outputFilePath);
		}else {
			Summarizer summarizer = new Summarizer(summarizerSettings);
			summarizer.run();
		}	
		log.info("Finished!");
	}

}
