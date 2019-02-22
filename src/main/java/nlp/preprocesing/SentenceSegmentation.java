package nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSegmentation {

	public static String[] segment(String text) {
		InputStream model = null;
		String sentences[] = null;
		// uses maximum entropy model 
		try {
			model = new FileInputStream("resources/models/pt-sent.bin");
			SentenceModel sm = new SentenceModel(model);
			SentenceDetectorME sd = new SentenceDetectorME(sm);
			sentences = sd.sentDetect(text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(model != null) {
				try {
					model.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return sentences;
	}
	
}
