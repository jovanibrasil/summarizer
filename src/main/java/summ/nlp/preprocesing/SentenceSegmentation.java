package summ.nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSegmentation implements Pipe<Text> {

	public Text segmentTextParagraph(Text text) {
		InputStream model = null;
		try {
			model = new FileInputStream("resources/models/pt-sent.bin");
			SentenceModel sm = new SentenceModel(model);
			// uses maximum entropy model 
			SentenceDetectorME sd = new SentenceDetectorME(sm);
			int globalPos = 0;
			for (Paragraph paragraph : text.getParagraphs()) {
				ArrayList<Sentence> sentences = new ArrayList<>();
				int localPos = 0;
				for (String s : sd.sentDetect(paragraph.getRawParagraph())) {
					
					Sentence sentence = new Sentence(s);
					sentence.setPos(localPos++);
					sentence.setId(globalPos++);
					if(paragraph.getPos() == 0) {
						sentence.setTitle(true);
					}
					
					sentences.add(sentence);
				}
				paragraph.setSentences(sentences);
			}
			
			
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

	@Override
	public Text process(Text text) {
		return this.segmentTextParagraph(text);
	}
	
}
