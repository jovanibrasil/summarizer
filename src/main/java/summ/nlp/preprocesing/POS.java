package summ.nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;

import summ.model.Text;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/*
 * Part-of-speech (POS) analysis identify the type of the words.
 * 
 * NN – noun, singular or mass
 * DT – determiner
 * VB – verb, base form
 * VBD – verb, past tense
 * VBZ – verb, third person singular present
 * IN – preposition or subordinating conjunction
 * NNP – proper noun, singular
 * TO – the word “to”
 * JJ – adjective
 * 
 * Complete list of tags:
 * 	https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
 * 
 */
public class POS {
	
	public static Text pos(Text text) {
		InputStream model = null;
		try {
			model = new FileInputStream("resources/models/pt-pos-perceptron.bin");
			POSModel posModel = new POSModel(model);
			// uses maximum entropy model 
			POSTaggerME posTagger = new POSTaggerME(posModel);
			
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					
					String tags[] = posTagger.tag(sentence.getRawWords());
					System.out.println(sentence.getWords().toString());
					for (String string : tags) {
						System.out.print(string + ", ");
					}
					System.out.println("\n");
					
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
