package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import model.Paragraph;
import model.Summary;
import model.Text;

public class Utils {

	public static Text loadText(String fileName) {
		BufferedReader br = null;
		
		Text text = null;
		
		try {
			File file = new File("projects/temario-2014/"+fileName);
			br = new BufferedReader(new FileReader(file));		
			String rawText = "";
			String line = null;

			text = new Text(rawText);
			
			int pos = 0;
			while((line = br.readLine()) != null) {
				Paragraph p = new Paragraph(line);
				p.setPos(pos++);
				text.addParagraph(p);
				rawText += line;
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
	
	
	public static Summary loadSummary(String fileName) {
		BufferedReader br = null;
		
		Summary summary = null;
		
		try {
			File file = new File("resources/documents/"+fileName);
			br = new BufferedReader(new FileReader(file));		
			String rawText = "";
			String line = null;

			summary = new Summary(rawText);
			
			int pos = 0;
			while((line = br.readLine()) != null) {
				Paragraph p = new Paragraph(line);
				p.setPos(pos++);
				summary.addParagraph(p);
				rawText += line;
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
		return summary;
	}
	
		
	
	
}
