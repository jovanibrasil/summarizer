package summ.utils;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

public class XmlUtils {

	public static void createConfigurationFile(String refSummaryPath, String genSummaryPath,
			String fileName, String outputPath) {
		try {
			refSummaryPath = refSummaryPath.substring(0, 2).equals("./") ? 
					refSummaryPath.substring(2) : refSummaryPath;
			File file = CustomFileUtils.getResourceAsFile("templates/rouge_configuration.xml");
			String htmlStr = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
			String dir = System.getProperty("user.dir") + "/";
			htmlStr = htmlStr.replace("reference_summary_path", dir + refSummaryPath + fileName)
				.replace("generated_summary_path", dir + genSummaryPath)
				.replace("reference_file_name", fileName + ".0.ref.html")
				.replace("generated_file_name", fileName + ".0.gen.html");
			
			File newHtmlFile = new File(outputPath + "/settings.xml");
			FileUtils.writeStringToFile(newHtmlFile, htmlStr, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
