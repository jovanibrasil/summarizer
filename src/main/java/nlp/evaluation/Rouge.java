package nlp.evaluation;

import com.rxnlp.tools.rouge.ROUGECalculator;
import com.rxnlp.tools.rouge.ROUGESettings;
import com.rxnlp.tools.rouge.SettingsUtil;

import model.Text;

public class Rouge {

	public static void evaluate(Text generatedSummary, Text referenceSummary) {

		ROUGESettings settings = new ROUGESettings();
		SettingsUtil.loadProps(settings);
		settings.PROJ_DIR = "projects/temario-2014";
		settings.USE_STEMMER = true;
		settings.REMOVE_STOP_WORDS = true;
		ROUGECalculator.computeRouge(settings);
		
	}
	
}
