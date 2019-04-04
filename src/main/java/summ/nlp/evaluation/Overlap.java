package summ.nlp.evaluation;

import java.util.HashMap;

import summ.model.Sentence;
import summ.model.Text;

public class Overlap {

	public static HashMap<String, HashMap<String, Object>>  evaluate(Text generatedText, Text referenceText) {
	
		HashMap<String, Object> result = new HashMap<>();
		
		int overlap = 0;
		for (Sentence s : generatedText.getSentences()) {			
			if(referenceText.containsSentence(s)) {
				//System.out.println("[ " + stats.overlap + " ] " + s.getRawSentence());
				overlap++;
			}
		}
		
		result.put("total_sentences", generatedText.getTotalSentence());
		result.put("overlap", overlap);
		
		HashMap<String, HashMap<String,Object>> finalResult = new HashMap<>();
		finalResult.put("result", result);
		return finalResult;
		
	}
	
}
