package summ.summarizer;

import java.awt.EventQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.optimization.Optimization;
import summ.gui.SummGUI;
import summ.settings.GlobalSettings;
import summ.settings.GlobalSettings.ExecutionType;

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
			
			String filesPath = summarizerSettings.OPTIMIZATION_PROPERTIES_PATH;
			for (String fileName : summarizerSettings.OPTIMIZATION_FILES) {
				summarizerSettings.OPTIMIZATION_PROPERTIES_PATH = filesPath + fileName;
				Optimization optmization = new Optimization(summarizerSettings);
				optmization.run();
			}			
			
		}else {
			Summarizer summarizer = new Summarizer(summarizerSettings);
			summarizer.run();
		}	
		log.info("Finished!");
	}

}
