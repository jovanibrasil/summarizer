package summ.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Paragraph;
import summ.model.Text;

public class FileUtils {
	
	private static final Logger log = LogManager.getLogger(FileUtils.class);

	public static List<Path> listTexts(String textsDir) {
		log.info("Loading list of texts from " + textsDir);
		try {
			Path filesPath = Paths.get(textsDir);
			if (Files.exists(filesPath)) {
				return Files.list(filesPath).collect(Collectors.toList());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Arrays.asList();
	}
	
	public static HashMap<String, Text> loadTexts(String textsDir) {
		log.info("Loading all texts from " + textsDir);
		HashMap<String, Text> texts = new HashMap<>();
		try {
			Path filesPath = Paths.get(textsDir);
			if (Files.exists(filesPath)) {
				List<Path> refFiles = Files.list(filesPath).collect(Collectors.toList());
				for (Path path2 : refFiles) {
					Text text  = loadText(textsDir + path2.getFileName().toString());
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
	
	public static List<Text> loadTexts(String textsDir, String referenceSummariesDir, int quantity) {
		log.info("Loading " + quantity + " texts from " + textsDir);
		List<Text> texts = new ArrayList<>();
		Random rand = new Random();
		try {
			if (Files.exists(Paths.get(textsDir))) {
				// Get a list of files in the directory
				List<Path> refFiles = Files.list(Paths.get(textsDir)).collect(Collectors.toList());
				while(texts.size() < quantity) {
					int nextIndex = rand.nextInt(refFiles.size());
					Path filePath = refFiles.remove(nextIndex);	
					Text text = loadText(textsDir + filePath.getFileName().toString());
					//String key = filePath.getFileName().toString();
					//key = key.contains("_") ? key.split("_")[0]+".txt" : key;
					
					text.setReferenceSummary(loadText(referenceSummariesDir + 
							text.getName().replace(".txt", "") + "_areference1.txt"));
					
					texts.add(text);
				}
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texts;
	}
	
	public static List<Text> loadTexts(String textsDir, int quantity) {
		log.info("Loading " + quantity + " texts from " + textsDir);
		List<Text> texts = new ArrayList<>();
		Random rand = new Random();
		try {
			if (Files.exists(Paths.get(textsDir))) {
				// Get a list of files in the directory
				List<Path> refFiles = Files.list(Paths.get(textsDir)).collect(Collectors.toList());
				while(texts.size() < quantity) {
					int nextIndex = rand.nextInt(refFiles.size()-1);
					Path filePath = refFiles.remove(nextIndex);	
					Text text = loadText(textsDir + filePath.getFileName().toString());
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
	
	public static Text loadText(String filePath) {
		log.debug("Loading " + filePath);
		BufferedReader br = null;
		Text text = null;
		try {
			File file = new File(filePath);
			br = new BufferedReader(new FileReader(file));		
			String rawText = "";
			String line = null;

			text = new Text(rawText);
			text.setName(file.getName());
			text.setFullTextPath(filePath);
			int pos = 0;
			while((line = br.readLine()) != null) {
				if(!line.isEmpty()) { // ignore empty lines
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
	
	public static InputStream loadProps(String propertyFilePath) {
		try {
			// Read properties file.
			log.info("Loading config.properties: " + propertyFilePath);
			InputStream stream = new FileInputStream(propertyFilePath);
			return stream;

		} catch (IOException exception) {
			System.err.println("Properties file not found. Please specify -Dopt.prop=<path_to_prop> in the command line.");
			System.exit(-1);
		}
		return null;
	}
	
	public static void createDir(String fileName) {
		try {
			log.info("Creating directory " + fileName);
			Path path = Paths.get(fileName);
		    Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveListOfObjects(List<Object> objList, String filePath) {
		try (PrintWriter out = new PrintWriter(filePath)) {
		    for (Object object : objList) {
		    	out.println(object);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static int countFiles(String textsDir) {
		try {
			return Files.list(Paths.get(textsDir)).collect(Collectors.toList()).size();
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			System.exit(-1);
		}
		return 0;
	}
	
}
