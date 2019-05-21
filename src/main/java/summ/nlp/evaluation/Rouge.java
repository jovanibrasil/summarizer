package summ.nlp.evaluation;

import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rxnlp.tools.rouge.ROUGECalculator;
import com.rxnlp.tools.rouge.ROUGESettings;
import com.rxnlp.tools.rouge.SettingsUtil;

import summ.model.Sentence;
import summ.model.Text;

public class Rouge implements EvaluationMethod {

	private static final Logger log = LogManager.getLogger(Rouge.class);
	private EvaluationTypes rougeType;
	private EvaluationTypes evaluationType;
	
	public Rouge(EvaluationTypes rougeType, EvaluationTypes evaluationType) {
		this.rougeType = rougeType;
		this.evaluationType = evaluationType;
	}	
	
	public EvaluationResult rougeEvaluation(Text generatedSummary, Text referenceSummary) {
		ROUGESettings settings = new ROUGESettings();
		SettingsUtil.loadProps(settings);
		settings.USE_STEMMER = false;
		settings.REMOVE_STOP_WORDS = false;
		HashMap<String, HashMap<String, Object>> rougeResult = ROUGECalculator.computeRouge(settings, 
				referenceSummary.getStringSentences(), generatedSummary.getStringSentences()); 
		EvaluationResult evalResult = formatRougeResult(rougeResult);
		evalResult.setMainEvaluationMetric(this.evaluationType);
		return evalResult; 
	}
	
	public EvaluationResult formatRougeResult(HashMap<String, HashMap<String, Object>> result) {

		EvaluationResult eval = new EvaluationResult();		
		
		result.keySet().forEach(key -> {
			HashMap<String, Object> r = result.get(key);
			eval.setEvalName(this.rougeType.name() + " evaluation - " + key);
			eval.addMetric(EvaluationTypes.PRECISION.name(), (Double)r.get("average_p"));
			eval.addMetric(EvaluationTypes.RECALL.name(), (Double)r.get("average_r"));
			eval.addMetric(EvaluationTypes.FMEASURE.name(), (Double)r.get("average_f"));
			
//			String resultStr = r.get("result_title") + "\t" + ((String)r.get("task_name")).toUpperCase() 
//				+ "\t" + ((String)r.get("result_name")).toUpperCase()
//			+ "\tAverage_R:" + r.get("average_r") + "\tAverage_P:" + r.get("average_p")
//			+ "\tAverage_F:" + r.get("average_f") + "\tNum Reference Summaries:" + r.get("reference_summary_count");
			
		});
		return eval;
	}
	
	/**
	 * 
	 * Calculates the number overlapped sentences between the texts. 
	 * 
	 * @param generatedText
	 * @param referenceText
	 * @return is a counter of overlaps.
	 */
	public int countOverlappedSentences(Text generatedText, Text referenceText) {
		int overlap = 0;
		for (Sentence s : generatedText.getSentences()) {			
			if(referenceText.containsSentence(s)) {
				overlap++;
			}
		}
		return overlap;
	}
	
	@Override
	public String toString() {
		return "Rouge";
	}

	@Override
	public EvaluationResult evaluate(Text generatedText, Text referenceText) {
		log.debug("Rouge evaluation ...");
		double relevantSentences = referenceText.getTotalSentence();
		double retrievedSentences = generatedText.getTotalSentence();
		double correctSentences = countOverlappedSentences(generatedText, referenceText); 
		
		EvaluationResult eval = rougeEvaluation(generatedText, referenceText);
		//eval.setMainEvaluationMetric(this.evaluationType);
		eval.addMetric("retrievedSentences", (double)retrievedSentences);
		eval.addMetric("relevantSentences", (double)relevantSentences);
		eval.addMetric("correctSentences", (double)correctSentences);
		return eval;
	}
	
}
