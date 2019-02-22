package nlp.preprocesing;

public class Preprocessing {
	
	public Preprocessing(String text) {
		String paragraphs[] = null;
		String sentences[] = null;
				
		// paragraph segmentation
		paragraphs	= ParagraphSegmentation.segment(text);
		
		// sentence segmentation
		if(paragraphs != null) {
			for (int i = 0; i < paragraphs.length; i++) {
				sentences = SentenceSegmentation.segment(paragraphs[i]);
			}	
		}
		
		// TODO punctuation remotion
		
		// TODO word segmentation (tokenization)
		if(sentences != null) {
			for (int j = 0; j < sentences.length; j++) {
				System.out.println(sentences[j]);
			}
		}
		
		// TODO stop words removing
		
	}
	
}
