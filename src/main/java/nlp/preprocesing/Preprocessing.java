package nlp.preprocesing;

import java.util.List;

import model.Text;

public class Preprocessing {
	

	public Text execute(Text text, OperationTypes op) {
		
		switch (op) {
			case PARAGRAPH_SEGMENTATION:
				return SentenceSegmentation.segmentTextParagraphs(text);
			case REMOVE_PUNCTUATION:
				return Utils.removePunctuation(text);
			case TO_LOWER_CASE:
				return Utils.convertToLowerCase(text);
			case SENTENCE_TOKENIZATION:
				return Tokenization.tokenizeTextSentences(text);	
			case REMOVE_STOPWORDS:
				return StopWords.removeStopWords(text);
			default:
				break;
		}
		return null;
		
	}
	
	
	public Preprocessing(Text text, List<OperationTypes> operations) {
		
		// procurar padrão de projetos para pipeline
		for (OperationTypes op : operations) {
			text = this.execute(text, op);		
		}
	
	}
	
}
