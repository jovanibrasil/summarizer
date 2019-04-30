package summ.nlp.preprocesing;

import summ.model.Text;
import summ.utils.Pipe;

public class PreprocessingFactory {

	public static Pipe<Text> getPreprocessing(PreProcessingTypes preprocessingType){
		switch (preprocessingType) {
			case IDENTIFY_TITLES:
				return new Titles();
			case NER:
				return new NER();
			case SIMPLE_TOKENIZATION:
				return new Tokenization(PreProcessingTypes.SIMPLE_TOKENIZATION);
			case WHITE_SPACE_TOKENIZATION:
				return new Tokenization(PreProcessingTypes.WHITE_SPACE_TOKENIZATION);
			case NEURAL_TOKENIZATION:
				return new Tokenization(PreProcessingTypes.NEURAL_TOKENIZATION);
			case POS:
				return new POSTagger();
			case REMOVE_PUNCTUATION:
				return new Misc(PreProcessingTypes.REMOVE_PUNCTUATION);
			case REMOVE_STOPWORDS:
				return new StopWords();
			case SENTENCE_SEGMENTATION:
				return new SentenceSegmentation();
			case TO_LOWER_CASE:
				return new Misc(PreProcessingTypes.TO_LOWER_CASE);
			case LEMMATIZATION:
				return new Lemmatizer();
			default:
				return null;
		}
	}
	
}
