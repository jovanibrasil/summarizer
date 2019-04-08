package summ.nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;

import summ.model.Sentence;
import summ.model.Text;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/*
 * Named entity recognition (NER)
 * 
 * The goal of named entity recognition is to find named entities like people, locations, organizations and 
 * other named things in a given text.
 * 
 * 
 */
public class NER {

	public static Text ner(Text text) {
		InputStream model = null;
		try {
			model = new FileInputStream("resources/models/en-ner-person.bin");
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
