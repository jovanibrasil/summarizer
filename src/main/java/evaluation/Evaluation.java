package evaluation;

import com.rxnlp.tools.rouge.ROUGECalculator;
import com.rxnlp.tools.rouge.ROUGESettings;
import com.rxnlp.tools.rouge.SettingsUtil;

public class Evaluation {

	public Evaluation() {
		// TODO Auto-generated constructor stub
		ROUGESettings settings = new ROUGESettings();
		SettingsUtil.loadProps(settings);
		settings.PROJ_DIR = "projects/temario-2014";
		settings.USE_STEMMER = true;
		settings.REMOVE_STOP_WORDS = true;

		// projects/test-summarization/reference
		// projects/test-summarization/reference
		// projects/test-summarization/system
		// projects/test-summarization/system
		
		ROUGECalculator.computeRouge(settings);

		
		
	}

}
