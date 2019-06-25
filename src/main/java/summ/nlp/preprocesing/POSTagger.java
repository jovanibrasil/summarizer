package summ.nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

/*
 * Part-of-speech (POS) analysis identify the type of the words.
 * 
 * NN noun, singular or mass
 * DT determiner
 * VB verb, base form
 * VBD verb, past tense
 * VBZ verb, third person singular present
 * IN preposition or subordinating conjunction
 * NNP proper noun, singular
 * TO the word “to”
 * JJ adjective
 * 
 * Complete list of tags:
 * 	https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
 * 
 */
public class POSTagger implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(POSTagger.class);
	
	public Text pos(Text text) {
		InputStream model = null;
		try {
			log.debug("Executing POSTagger for each word in the text " + text.getName());
			model = new FileInputStream("src/main/resources/models/pt-pos-perceptron.bin");
			POSModel posModel = new POSModel(model);
			// uses maximum entropy model 
			POSTaggerME posTagger = new POSTaggerME(posModel);
			
			for (Sentence sentence : text.getSentences()) {
				String tags[] = posTagger.tag(sentence.getRawWords());
				for (int i = 0; i < tags.length; i++) {
					sentence.getWords().get(i).setPosTag(tags[i]);
				}
			}
			
		} catch (Exception e) {
			log.warn("Problem with POSTagger model file." + e.getMessage());
			log.warn("If you want to use POSTagger, please fix this issue first before proceeding.");
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
		return "POSTagger";
	}
	
	@Override
	public Text process(Text text) {
		return this.pos(text);
	}
	
}
