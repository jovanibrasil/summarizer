package summ.nlp.preprocesing;

import java.io.InputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import summ.model.Text;
import summ.utils.Pipe;

/*
 * Named entity recognition (NER)
 * 
 * The goal of named entity recognition is to find named entities like people, locations, organizations and 
 * other named things in a given text.
 * 
 * 
 */
public class NER implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(NER.class);

	public Text ner(Text text) {
		InputStream model = null;
		try {
			log.debug("Executing NER (named entity recognition) for each sentence in " + text.getName());
			model = ClassLoader.getSystemClassLoader().getResourceAsStream("models/en-ner-person.bin");
			TokenNameFinderModel tokenFinderModel = new TokenNameFinderModel(model);
			// uses maximum entropy model 
			NameFinderME namefinder = new NameFinderME(tokenFinderModel);
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(s -> {
					
					Span[] spans = namefinder.find(s.getRawWords());
					// return a list with the indices of the tokens which compose named entities in the text 
					for (Span span : spans) {
						System.out.println(s.getInitialValue());
						System.out.println(span);
					}
					
				});
			});
			
		} catch (Exception e) {
			log.warn("Problem with NER model file." + e.getMessage());
			log.warn("If you want to use NER, please fix this issue first before proceeding.");
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
		return "NER";
	}
	
	@Override
	public Text process(Text text) {
		return ner(text);
	}
	
	
}
