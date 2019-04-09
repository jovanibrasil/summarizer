package summ.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import summ.model.Sentence;
import summ.model.Text;
import summ.nlp.features.FeatureType;

public class ExportHTML {
	
	/**
	 * 
	 * Exporta uma lista de sentenças com os respectivos valores de suas features.
	 * 
	 * @param text
	 * @param selectedFeatures
	 */
	public static void exportSentecesAndFeatures(Text text, List<String> selectedFeatures) {
		try {
			File file = new File("resources/templates/result.html") ;
			String htmlString = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
			
			String title = text.getName(), body = "<ol>";
			
			for (Sentence sentence : text.getSentences()) {
				body += "<li>" 
						+ sentence.getInitialValue(); // Original String
				body += "<ul>"
						+ " <li>" + sentence.getWords().toString() + "</li>" // Final processed string
						+ " <li>" + sentence.getFeatures(selectedFeatures).toString() + "</li>" // Selected string features
						+ " <li> Score: " + sentence.getScore()  + "<li>"
						+ "</ul>"
						+ "</li>";
			}
			body += "</ol>";
			
			htmlString = htmlString.replace("$title", title);
			htmlString = htmlString.replace("$body", body);
			
			File newHtmlFile = new File("results/texts-evaluation-features" + (new Date()).toString() + ".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 * Exporta o texto reescrito e marcado com as sentenças escolhidas.
	 * 
	 * @param text
	 * @param selectedSentences
	 */
	public static void exportHighlightText(Text text, Map<Integer, Sentence> selectedSentences) {
		try {
			File file = new File("resources/templates/result.html") ;
			String htmlString = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
			
			String title = text.getName(), body = "<ol>";
			
			for (Sentence sentence : text.getSentences()) {
				String rawSentence = sentence.getInitialValue();
				body += "<li>"; 
				body += selectedSentences.containsKey(sentence.getId()) ? "<mark>" + rawSentence + "</mark>" : rawSentence; // Original String
				body += "</li>";
			}
			body += "</ol>";
			
			htmlString = htmlString.replace("$title", title);
			htmlString = htmlString.replace("$body", body);
			
			File newHtmlFile = new File("results/texts-evaluation-features" + (new Date()).toString() + ".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
