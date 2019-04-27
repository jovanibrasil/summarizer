package summ.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Paragraph;
import summ.model.Text;

public class Utils {
	
	private static final Logger log = LogManager.getLogger(Utils.class);

	public static HashMap<String, Text> loadTexts(String textsDir, TextType textType) {
		log.info("Loading all texts from" + textsDir);
		HashMap<String, Text> texts = new HashMap<>();
		try {
			Path filesPath = Paths.get(textsDir);
			if (Files.exists(filesPath)) {
				List<Path> refFiles = Files.list(filesPath).collect(Collectors.toList());
				for (Path path2 : refFiles) {
					Text text  = loadText(textsDir, path2.getFileName().toString());
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
	
	public static List<Text> loadTexts(String textsDir, TextType textType, int quantity) {
		log.info("Loading " + quantity + "texts from" + textsDir);
		List<Text> texts = new ArrayList<>();
		Random rand = new Random();
		try {
			if (Files.exists(Paths.get(textsDir))) {
				// Get a list of files in the directory
				List<Path> refFiles = Files.list(Paths.get(textsDir)).collect(Collectors.toList());
				while(texts.size() < quantity) {
					int nextIndex = rand.nextInt(refFiles.size()-1);
					Path filePath = refFiles.remove(nextIndex);	
					Text text = loadText(textsDir, filePath.getFileName().toString());
					//String key = filePath.getFileName().toString();
					//key = key.contains("_") ? key.split("_")[0]+".txt" : key;
					texts.add(text);	
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texts;
	}
	
	public static Text loadText(String filePath, String fileName) {
		log.info("Loading " + fileName);
		BufferedReader br = null;
		Text text = null;
		try {
			File file = new File(filePath + fileName);
			br = new BufferedReader(new FileReader(file));		
			String rawText = "";
			String line = null;

			text = new Text(rawText);
			text.setName(fileName);
			text.setFullTextPath(filePath + fileName);
			int pos = 0;
			while((line = br.readLine()) != null) {
				if(!line.isEmpty()) {
					line = line.replace("(...)", "").replace("...", "")
							.replace("....", "").replace(".,", ",");
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
