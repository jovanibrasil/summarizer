package nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import model.Sentence;
import model.Text;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSegmentation {

	public static Text segmentTextParagraphs(Text text) {
		InputStream model = null;
		try {
			model = new FileInputStream("resources/models/pt-sent.bin");
			SentenceModel sm = new SentenceModel(model);
			// uses maximum entropy model 
			SentenceDetectorME sd = new SentenceDetectorME(sm);
			
			text.getParagraphs().forEach( paragraph -> {
				ArrayList<Sentence> sentences = new ArrayList<>();
				for (String s : sd.sentDetect(paragraph.getRawParagraph())) {
					sentences.add(new Sentence(s));
				}
				paragraph.setSentences(sentences);
			});
			
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
		return text;
	}
	
}
