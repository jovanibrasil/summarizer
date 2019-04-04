package summ.nlp.preprocesing;

import java.io.FileInputStream;
import java.io.InputStream;

import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

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
	
	public Text pos(Text text) {
		InputStream model = null;
		try {
			model = new FileInputStream("resources/models/pt-pos-perceptron.bin");
			POSModel posModel = new POSModel(model);
			// uses maximum entropy model 
			POSTaggerME posTagger = new POSTaggerME(posModel);
			
			for (Sentence sentence : text.getSentences()) {
				String tags[] = posTagger.tag(sentence.getRawWords());
				
				for (int i = 0; i < tags.length; i++) {
					//System.out.println(sentence.getRawWords()[i] + " : " + tags[i]);
					sentence.getWords().get(i).setPosTag(tags[i]);
					//System.out.println("\n");
				}
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
		return this.pos(text);
	}
	
}
