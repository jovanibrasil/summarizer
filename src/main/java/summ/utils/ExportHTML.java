package summ.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import summ.model.Sentence;
import summ.model.Text;

public class ExportHTML {
	
	/**
	 * 
	 * Exporta uma lista de sentencas com os respectivos valores de suas features.
	 * 
	 * @param text
	 * @param selectedFeatures
	 */
	public static void exportSentecesAndFeatures(Text text, List<String> selectedFeatures, String outputPath, String fileName) {
		try {
			File file = new File("src/main/resources/templates/result.html") ;
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
			
			File newHtmlFile = new File(outputPath + "/" + text.getName().replace(".txt", "") + 
					"-" + fileName + "-pp-and-features" + Utils.generateStringFormattedData() + ".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *
	 * Exporta o texto reescrito e marcado com as sentencas escolhidas.
	 * 
	 * @param text
	 * @param selectedSentences
	 */
	public static void exportHighlightText(Text text, Map<Integer, Sentence> selectedSentences, String outputPath) {
		try {
			File file = new File("src/main/resources/templates/result.html") ;
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
			
			File newHtmlFile = new File(outputPath + "/highlighted-select-sentences-" + Utils.generateStringFormattedData() + ".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportOverlappedFeatures(Text text1, Text text2, String outputPath) {

		try {
			File file = new File("src/main/resources/templates/result.html") ;
			String htmlString = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
			
			String title = text1.getName() + "_" + text2.getName(), body = "<ol>";
			
			for (Sentence s : text1.getSentences()) {			
				if(text2.containsSentence(s)) {
					body += "<li>" + s.getInitialValue() + "</li>";
				}
			}
			
			body += "</ol>";
			
			htmlString = htmlString.replace("$title", title);
			htmlString = htmlString.replace("$body", body);
			
			File newHtmlFile = new File(outputPath + "/overlapped-sentences-" + Utils.generateStringFormattedData() + ".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
