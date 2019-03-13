package summ.nlp.evaluation;

import java.util.HashMap;

import com.rxnlp.tools.rouge.ROUGECalculator;
import com.rxnlp.tools.rouge.ROUGESettings;
import com.rxnlp.tools.rouge.SettingsUtil;

import summ.model.Text;

public class Rouge {

	public static HashMap<String, HashMap<String, Object>> evaluate(Text generatedSummary, Text referenceSummary) {

		ROUGESettings settings = new ROUGESettings();
		SettingsUtil.loadProps(settings);
//		settings.PROJ_DIR = "projects/temario-2014";
//		settings.USE_STEMMER = true;
//		settings.REMOVE_STOP_WORDS = true;
		HashMap<String, HashMap<String, Object>> result = ROUGECalculator.computeRouge(settings);
		formatRougeResult(result);
		return result;
		
	}
	
	public static void formatRougeResult(HashMap<String, HashMap<String, Object>> result) {
//		
//		res.put("result_title", str);
//		res.put("ngram", ngram);
//		res.put("task_name", t.taskName);
//		res.put("result_name", r.name);
//		res.put("average_p", r.precision);
//		res.put("average_r", r.recall);
//		res.put("average_f", r.f);
//		res.put("reference_summary_count", r.count);
		result.keySet().forEach(key -> {
			HashMap<String, Object> r = result.get(key);
			
			String resultStr = r.get("result_title") + "\t" + ((String)r.get("task_name")).toUpperCase() 
				+ "\t" + ((String)r.get("result_name")).toUpperCase()
			+ "\tAverage_R:" + r.get("average_r") + "\tAverage_P:" + r.get("average_p")
			+ "\tAverage_F:" + r.get("average_f") + "\tNum Reference Summaries:" + r.get("reference_summary_count");
			
			System.out.println(resultStr);
			
		});
		
	}
	
}
