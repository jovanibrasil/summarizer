package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import model.Paragraph;
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

			res = new Text(rawText);
			
//			line = br.readLine();
//			res.setTitle(line);
			
			int pos = 0;
			while((line = br.readLine()) != null) {
				Paragraph p = new Paragraph(line);
				p.setPos(pos++);
				paragraphs.add(p);
				rawText += line;
			}
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
