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
	
	public GlobalSettings() {
		loadSummarizerProps();
	}
	
	public void loadSummarizerProps() {
		log.info("Loading summarizer properties ...");
		InputStream stream = FileUtils.loadProps("/home/jovani/workspace/summarizer/src/main/resources/settings/summarizer.properties");
		Properties properties = new Properties();
		try {
			properties.load(stream);
			String val = properties.getProperty("execution_type", "SUMMARIZATION");
			this.EXECUTION_TYPE = ExecutionType.valueOf(val);
			this.SUMMARIZATION_PROPERTIES_PATH = properties.getProperty("summarization_properties_path", "");
			this.OPTIMIZATION_PROPERTIES_PATH = properties.getProperty("optimization_properties_path", "");
			this.OUTPUT_PATH = properties.getProperty("output_path", "");
			this.OPTIMIZATION_FILES = properties.getProperty("optimization_files", "").replace(" ", "").split(",");
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
