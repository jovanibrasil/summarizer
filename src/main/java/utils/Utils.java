package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

	public static String readFile(String fileName) {
		BufferedReader br = null;
		String res = null;
		try {
			File file = new File("resources/documents/"+fileName);
			br = new BufferedReader(new FileReader(file));
			res = br.lines().collect(Collectors.joining("\n"));
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
