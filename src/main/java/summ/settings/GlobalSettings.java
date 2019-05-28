package summ.settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import summ.utils.FileUtils;

public class GlobalSettings {
	
	static Logger log = Logger.getLogger(GlobalSettings.class);
	
	public enum ExecutionType {
		OPTIMIZATION, SUMMARIZATION 
	}
	
	public ExecutionType EXECUTION_TYPE;
	public String SUMMARIZATION_PROPERTIES_PATH;
	public String OPTIMIZATION_PROPERTIES_PATH;
	public String OUTPUT_PATH;
	public String[] OPTIMIZATION_FILES;
	
	public String CORPUS_PATH;
	public String AUTO_SUMMARIES_PATH;
	public double TRAINING_TEXTS_PERCENTUAL;
	public String MANUAL_SUMMARIES_PATH;
	
	public GlobalSettings() {
		loadSummarizerProps();
	}
	
	public void loadSummarizerProps() {
		log.info("Loading summarizer properties ...");
		InputStream stream = FileUtils.loadProps("./settings/summarizer.properties");
		Properties properties = new Properties();
		try {
			properties.load(stream);
			String val = properties.getProperty("execution_type", "SUMMARIZATION");
			this.EXECUTION_TYPE = ExecutionType.valueOf(val);
			this.SUMMARIZATION_PROPERTIES_PATH = properties.getProperty("summarization_properties_path", "");
			this.OPTIMIZATION_PROPERTIES_PATH = properties.getProperty("optimization_properties_path", "");
			this.OUTPUT_PATH = properties.getProperty("output_path", "");
			this.OPTIMIZATION_FILES = properties.getProperty("optimization_files", "").replace(" ", "").split(",");
			
			this.CORPUS_PATH = properties.getProperty("corpus_path", "");
			this.AUTO_SUMMARIES_PATH = properties.getProperty("auto_summaries_path", "");
			this.MANUAL_SUMMARIES_PATH = properties.getProperty("manual_summaries_path", "");
			this.TRAINING_TEXTS_PERCENTUAL = 0.8; //Double.parseDouble(properties.getProperty("evaluation_len", "0.0")); TODO
			
		} catch (IOException e) {
			log.warn("Problem with settings model file." + e.getMessage());
			log.warn("If you want to use the tool, please fix this issue first before proceeding.");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Summarizer settings:")
			.append("\n\tExecution type: ").append(this.EXECUTION_TYPE)
			.append("\n\tSummarization properties path: ").append(this.SUMMARIZATION_PROPERTIES_PATH)		
			.append("\n\tOptimization properties path: ").append(this.OPTIMIZATION_PROPERTIES_PATH);
		return sb.toString();
	}

}
