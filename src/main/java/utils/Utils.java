package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import model.Paragraph;
import model.Sentence;
import model.Text;

public class Utils {

	public static Text readFile(String fileName) {
		BufferedReader br = null;
		
		Text res = null;
		
		try {
			File file = new File("resources/documents/"+fileName);
			br = new BufferedReader(new FileReader(file));		
			String rawText = "";
			ArrayList<Paragraph> paragraphs = new ArrayList<>();
			String line = null;
			while((line = br.readLine()) != null) {
				paragraphs.add(new Paragraph(line));
				rawText += line;
			}
			res = new Text(rawText);
			res.setParagraphs(paragraphs);			
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
		return res;
	}
	
}
