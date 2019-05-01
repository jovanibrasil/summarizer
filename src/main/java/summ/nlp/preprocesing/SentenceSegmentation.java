package summ.nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

public class SentenceSegmentation implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(SentenceSegmentation.class);
	
	public Text segmentSentences(Text text) {
		InputStream model = null;
		try {
			log.info("Segmenting text into sentences.");
			model = new FileInputStream("src/main/resources/models/pt-sent.bin");
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
			log.warn("problem with segmentation model file." + e.getMessage());
			log.warn("If you want to use segmentation, please fix this issue first before proceeding.");
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
	public String toString() {
		return "Sentence segmentation";
	}
	
	@Override
	public Text process(Text text) {
		return this.segmentSentences(text);
	}
	
}
