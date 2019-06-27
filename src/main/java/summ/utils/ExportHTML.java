package summ.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;

public class ExportHTML {
	
	private static final Logger log = LogManager.getLogger(ExportHTML.class);
	
	/**
	 * 
	 * Exporta uma lista de sentencas com os respectivos valores de suas features.
	 * 
	 * @param text
	 * @param selectedFeatures
	 */
	public static void exportSentecesAndFeatures(Text text, List<String> selectedFeatures, String outputPath, String fileName) {
		try {
			File file = CustomFileUtils.getResourceAsFile("templates/result.html");
			String htmlString = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
			
			String title = text.getName(), body = "<ol>";
			
			for (Sentence sentence : text.getSentences()) {
				body += "<li>" 
						+ sentence.getInitialValue(); // Original String
				body += "<ul>"
						+ " <li>" + sentence.getWordsToStringWithFeatures() + "</li>" // Final processed string
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
			File file = CustomFileUtils.getResourceAsFile("templates/result.html");
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
			
			File newHtmlFile = new File(outputPath + "/highlighted-select-sentences-"
							+ Utils.generateStringFormattedData() + ".html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportOverlappedFeatures(Text text1, Text text2, String outputPath) {

		try {
			File file = CustomFileUtils.getResourceAsFile("templates/result.html");
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

	public static void exportSummaryRougeFormat(Text summary, String outputPath) {
		log.debug("Saving summary (Rouge format)");
		try {
			String sentences = "";
			
			File file = CustomFileUtils.getResourceAsFile("templates/rouge_summary.html");
			String htmlString = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
			
			int id = 0;
			for (Sentence s : summary.getSentences()) {			
				sentences += "<a name=\"" + id + "\">[" + id + "]</a> <a href=\"#" + id 
					+ "\" id=" + id + ">" + s.getInitialValue() + "</a>\n";
				id++;
			}
			
			htmlString = htmlString.replace("filename", summary.getName() + ".0.gen.html");//summary.getName());
			htmlString = htmlString.replace("sentences", sentences);
			
			File newHtmlFile = new File(outputPath + "/" + summary.getName() + ".0.gen.html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
