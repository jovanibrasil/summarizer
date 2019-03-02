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
			case TOKENIZATION:
				return Tokenization.tokenization(text);	
			case REMOVE_STOPWORDS:
				return StopWords.removeStopWords(text);
			case POS:
				return POS.pos(text);
			case NER:
				return NER.ner(text);
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
