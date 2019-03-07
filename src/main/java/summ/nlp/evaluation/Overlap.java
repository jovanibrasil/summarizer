package summ.nlp.evaluation;

import summ.model.Text;

public class Overlap {

	public static void evaluate(Text generatedText, Text referenceText) {
	
		var stats = new Object() { int overlap = 0; };
		generatedText.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				if(referenceText.containsSentence(s)) {
					System.out.println("[ " + stats.overlap + " ] " + s.getRawSentence());
					stats.overlap++;
				}
			});
		});
		
		System.out.println("Total sentences");
		System.out.println("	Generated summary: " + generatedText.getTotalSentence());
		System.out.println("	Reference summary: " + referenceText.getTotalSentence());
		System.out.println("Overlap stat: " + stats.overlap);
		
	}
	
}
