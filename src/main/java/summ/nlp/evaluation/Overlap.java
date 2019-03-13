package summ.nlp.evaluation;

import java.util.HashMap;

import summ.model.Text;

public class Overlap {

	public static HashMap<String, HashMap<String, Object>>  evaluate(Text generatedText, Text referenceText) {
	
		HashMap<String, Object> result = new HashMap<>();
		
		var stats = new Object() { int overlap = 0; };
		generatedText.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				if(referenceText.containsSentence(s)) {
					//System.out.println("[ " + stats.overlap + " ] " + s.getRawSentence());
					stats.overlap++;
				}
			});
		});
		
		result.put("total_sentences", generatedText.getTotalSentence());
		result.put("overlap", stats.overlap);
		
		HashMap<String, HashMap<String,Object>> finalResult = new HashMap<>();
		finalResult.put("result", result);
		return finalResult;
		
	}
	
}
