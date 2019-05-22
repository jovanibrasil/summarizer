package summ.nlp.evaluation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

//import com.rxnlp.tools.rouge.ROUGECalculator;
//import com.rxnlp.tools.rouge.ROUGESettings;
//import com.rxnlp.tools.rouge.SettingsUtil;

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
	
	public EvaluationResult rougeEvaluation(String outputPath) {
		EvaluationResult eval = new EvaluationResult();
		try {

			// Command to create an external process
			String projectDir = System.getProperty("user.dir");
			String rougePath = projectDir + "/ROUGE-1.5.5";
			String dataPath = rougePath + "/data";
			String settingsPath = projectDir + "/" + outputPath + "/settings.xml";
			
			/*
			 * -v	verbose mode
			 * -e	rouge files directory
			 * -n 	specifies ROUGE-N
			 * -x	ROUGE-L
			 * -c	confiability interval 
			 * -r
			 * -f
			 * -p	
			 * -a	settings path - specifies which systems we want to evaluate 
			 * -m 	specifies the usage of stemming
			 * 
			 */
			
			String[] command = {rougePath + "/ROUGE-1.5.5.pl" , 
					"-e" , dataPath,
					"-n", "1",
					"-x",
					"-c", "95", 
					"-r", "1000", 
					"-f", "A",
					"-p", "0.5", 
					"-a", settingsPath
					};
			
			// Running the above command
			Runtime run = Runtime.getRuntime();
			Process proc = run.exec(command);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			// read the output from the command
			log.debug("Standard output of the command:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				log.debug(s);
				String[] values = s.toLowerCase().replace(")", "").replace(":", "")
						.replace("-", "_").split(" ");
				if(values.length == 8) {
					eval.addMetric(getMetricName(values[2]), Double.parseDouble(values[3]));
					eval.addMetric(values[1]+values[2]+"-min-interval", Double.parseDouble(values[5]));
					eval.addMetric(values[1]+values[2]+"-max-interval", Double.parseDouble(values[7]));
				}
			}

			// read any errors from the attempted command
			log.debug("Standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				log.info(s);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return eval;
	}
	
	public String getMetricName(String metricName) {
		switch (metricName) {
		case "average_r":
			return EvaluationTypes.RECALL.name();
		case "average_p":
			return EvaluationTypes.PRECISION.name();
		case "average_f":
			return EvaluationTypes.FMEASURE.name();
		default:
			return "";
		}
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
	public EvaluationResult evaluate(Text generatedText, Text referenceText, String outputPath) {
		log.debug("Rouge evaluation ...");
		double relevantSentences = referenceText.getTotalSentence();
		double retrievedSentences = generatedText.getTotalSentence();
		double correctSentences = countOverlappedSentences(generatedText, referenceText); 
		
		EvaluationResult eval = rougeEvaluation(outputPath);
		//eval.setMainEvaluationMetric(this.evaluationType);
		eval.addMetric("retrievedSentences", (double)retrievedSentences);
		eval.addMetric("relevantSentences", (double)relevantSentences);
		eval.addMetric("correctSentences", (double)correctSentences);
		return eval;
	}
	
}
