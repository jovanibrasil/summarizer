package summ.settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import summ.model.Text;
import summ.nlp.evaluation.EvaluationMethod;
import summ.nlp.evaluation.EvaluationMethodFactory;
import summ.nlp.evaluation.EvaluationMethodFactory.EvaluationMethodType;
import summ.utils.Pipeline;
import summ.utils.PipelineFactory;
import summ.utils.Utils;

public class SummarizationSettings {
	
	
	static Logger log = Logger.getLogger(SummarizationSettings.class);
	
	public SummarizationType SUMMARIZATION_TYPE;
	
	public String FULL_TEXTS_PATH;
	public String MANUAL_SUMMARIES_PATH;
	public String AUTO_SUMMARIES_PATH;
	public String FUZZY_SYSTEM_PATH;

	public Pipeline<Text> TEXT_PREPROCESSING_PIPELINE;
	public Pipeline<Text> SUMMARY_PREPROCESSING_PIPELINE;
	public Pipeline<Text> FEATURES_PIPELINE;

	public double SUMMARY_SIZE_PERCENTUAL;
	public EvaluationMethod EVALUATION_METHOD;
	public OutputType OUTPUT_TYPE;
	public List<String> OUTPUT_EXPORT_VARIABLES;

	public List<String> VAR_NAMES;
	public String TEXT_NAME;
	
	public SummarizationSettings(String propertyFilePath) {
		this.loadSummarizationProps(propertyFilePath);
	}
	
	public void loadSummarizationProps(String propertyFilePath) {
		log.info("Loading summarization properties ...");
		InputStream stream = Utils.loadProps(propertyFilePath);
		Properties properties = new Properties();
		try {
			properties.load(stream);
			this.FULL_TEXTS_PATH = properties.getProperty("full_texts_path", "");
			this.MANUAL_SUMMARIES_PATH = properties.getProperty("manual_summaries_path");
			this.AUTO_SUMMARIES_PATH = properties.getProperty("auto_summaries_path", "");
			String[] val = properties.getProperty("text_preprocessing_pipeline", "").replace(" ", "").split(",");
			this.TEXT_PREPROCESSING_PIPELINE = PipelineFactory.getPreprocessingPipeline(val);
			val = properties.getProperty("summary_preprocessing_pipeline", "").replace(" ", "").split(",");
			this.SUMMARY_PREPROCESSING_PIPELINE = PipelineFactory.getPreprocessingPipeline(val);
			val = properties.getProperty("feature_pipeline", "").replace(" ", "").split(",");
			this.FEATURES_PIPELINE = PipelineFactory.getFeaturesPipeline(val);
			String strVal = properties.getProperty("summary_size_percentual", "0");
			this.SUMMARY_SIZE_PERCENTUAL = Double.parseDouble(strVal);
			strVal = properties.getProperty("evaluation_method", "NONE");
			this.EVALUATION_METHOD = EvaluationMethodFactory.getEvaluationMethod(EvaluationMethodType.valueOf(strVal));
			strVal = properties.getProperty("output_type", "NONE");
			this.OUTPUT_TYPE = OutputType.valueOf(strVal);
			if(this.OUTPUT_TYPE.equals(OutputType.FEATURES_BY_SENTENCE_HTML)) {
				val = properties.getProperty("output_export_variables", "").replace(" ", "").split(",");
				this.OUTPUT_EXPORT_VARIABLES = Arrays.asList(val);
			}
			strVal = properties.getProperty("summarization_type", "SINGLE");
			this.SUMMARIZATION_TYPE = SummarizationType.valueOf(strVal);
			if(this.SUMMARIZATION_TYPE.equals(SummarizationType.SINGLE)) {
				this.TEXT_NAME = properties.getProperty("file_name", "");
			}
			this.FUZZY_SYSTEM_PATH = properties.getProperty("fuzzy_system_path", "");
			
			strVal = properties.getProperty("var_names", "").replace(" ", "");
			if(strVal.isEmpty()) {
				this.VAR_NAMES = new ArrayList<>();
			} else {
				this.VAR_NAMES = Arrays.asList(strVal.split(","));
			}
			
		} catch (IOException e) {
			log.warn("Problem with settings model file." + e.getMessage());
			log.warn("If you want to use the tool, please fix this issue first before proceeding.");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Summarization settings:")
			.append("\n\tFull texts path: ").append(this.FULL_TEXTS_PATH)
			.append("\n\tManual summaries path: ").append(this.MANUAL_SUMMARIES_PATH)		
			.append("\n\tAuto summaries path: ").append(this.AUTO_SUMMARIES_PATH)
			.append("\n\tText preprocessing pipeline: ").append(this.TEXT_PREPROCESSING_PIPELINE)
			.append("\n\tSummaries preprocessing pipeline: ").append(this.SUMMARY_PREPROCESSING_PIPELINE)
			.append("\n\tFeatures pipeline: ").append(this.FEATURES_PIPELINE);
		return sb.toString();
	}
	
	public enum OutputType {
		CONSOLE, HIGHLIGHTED_TEXT, FEATURES_BY_SENTENCE_CSV, OVERLAPPED_SENTENCES_HTML, FEATURES_BY_SENTENCE_HTML, NONE
	}
	
	public enum SummarizationType {
		SINGLE, MULTIPLE
	}
	
}
