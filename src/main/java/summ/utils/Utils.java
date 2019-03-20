package summ.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import summ.model.Paragraph;
import summ.model.Text;

public class Utils {

	public static HashMap<String, Text> loadTexts(String path, TextType textType) {
		HashMap<String, Text> texts = new HashMap<>();
		try {
			Path filesPath = Paths.get(path);
			if (Files.exists(filesPath)) {
				List<Path> refFiles = Files.list(filesPath).collect(Collectors.toList());
				for (Path path2 : refFiles) {
					Text text  = loadText(path, path2.getFileName().toString());
					String key = path2.getFileName().toString();
					key = key.contains("_") ? key.split("_")[0]+".txt" : key;
					texts.put(key, text);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texts;
	}
	
	public static Text loadText(String filePath, String fileName) {
		BufferedReader br = null;
		
		Text text = null;
		
		try {
			File file = new File(filePath + fileName);
			br = new BufferedReader(new FileReader(file));		
			String rawText = "";
			String line = null;

			text = new Text(rawText);
			text.setName(fileName);
			int pos = 0;
			while((line = br.readLine()) != null) {
				if(!line.isEmpty()) {
					line = line.replace("(...)", "").replace("...", "")
							.replace("....", "");
					Paragraph p = new Paragraph(line);
					p.setPos(pos++);
					text.addParagraph(p);
					rawText += line;
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return text;
	}
	
	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public static double convertCosineToAngle(double consine) {
		return Math.acos(consine);
	}
	
}
