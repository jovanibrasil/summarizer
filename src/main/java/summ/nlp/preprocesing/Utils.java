package summ.nlp.preprocesing;

import summ.model.Text;

public class Utils {
	
	public static Text removePunctuation(Text text) {
		
		try {
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					String editedSentence = sentence.getEditedSentence().replaceAll("\\p{Punct}", "");
					sentence.setEditedSentence(editedSentence);
				});
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return text;
	}
	
	public static Text convertToLowerCase(Text text) {
		
		try {
			text.getParagraphs().forEach( paragraph -> {
				paragraph.getSentences().forEach(sentence -> {
					String editedSentence = sentence.getEditedSentence().toLowerCase();
					sentence.setEditedSentence(editedSentence);
				});
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return text;
	}
	
}
