package nlp.preprocesing;

import model.Text;

public class Preprocessing {
	
	public Preprocessing(Text text) {
		
		// procurar padrão de projetos para pipeline
		
		// sentence segmentation
		text = SentenceSegmentation.segmentTextParagraphs(text);
		
		// TODO punctuation remotion
		text = Utils.removePunctuation(text);
		
		// TODO convert to lower case
		text = Utils.convertToLowerCase(text);
		
		// TODO word segmentation (tokenization)
		text = Tokenization.tokenizeTextSentences(text);
	
		// TODO stop words removing
		StopWords sw = new StopWords("resources/stopwords-pt-br.txt");
		text = StopWords.removeStopWords(text);
		
		text.getParagraphs().forEach(p -> {
			
			System.out.println("----------------");
			System.out.println(p);
			
			p.getSentences().forEach(s -> {
				
				System.out.println("row sentence: " + s.getRawSentence());
				System.out.println("edited sentence: " + s.getEditedSentence());
				System.out.println("valid words" + s.getWords().toString());
				
			});
			
			
		});
		
	}
	
}
