package nlp.preprocesing;

public class ParagraphSegmentation {

	public static String[] segment(String text) {
		String paragraphs[] = text.split("\n");
		return paragraphs;
	}
	
}
