package summ.nlp.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class Frequency implements Pipe<Text> {
	
	private static final Logger log = LogManager.getLogger(Frequency.class);
	
	/**
	 * The most simple term frequency (also called raw frequency) is denoted by the number of 
	 * times that the term occurs in the document. 
	 * 
	 * This implementation has two variations: 
	 * 
	 * (1) term frequency adjusted for document length: the raw term frequency is divided
	 * by the document length (number of words in the document).	
	 * 
	 * (2) term frequency adjusted by augmented frequency: the term frequency is divided  
	 * by the raw frequency of the most occurring term in the document.
	 *   
	 */
	public void tf(Text text) {
		
		log.debug("Calculating TF (term frequency).");
		
		HashMap<String, Integer> rtf = new HashMap<String, Integer>(); 
		int wordCounter = 0; String maxRtfKey = ""; 
		rtf.put(maxRtfKey, 0);
		for (Paragraph p : text.getParagraphs()) {
			for (Sentence s : p.getSentences()) {
				for (Word w : s.getWords()) {
					// increment word counter
					wordCounter++;
					// increment frequency counter
					String key = w.getCurrentValue();
					int value = rtf.containsKey(key) ? rtf.get(key) + 1 : 1;
					rtf.put(key, value);
					// update max frequency
					maxRtfKey = rtf.get(key) > rtf.get(maxRtfKey) ? key : maxRtfKey;	
				}
			}
		}
		rtf.remove("");
		
		Map<String, Double> tfDocLenBased = new HashMap<>();
		for (Entry<String, Integer> e : rtf.entrySet()) {
			tfDocLenBased.put(e.getKey(), (double)e.getValue() / wordCounter);
		}
		
		Map<String, Double> tfMaxRtfBased = new HashMap<>();
		for (Entry<String, Integer> e : rtf.entrySet()) {
			tfMaxRtfBased.put(e.getKey(), (double)e.getValue() / rtf.get(maxRtfKey));
		}
		
		text.addFeature("doc_length", wordCounter); // 
		text.addFeature("rtf", rtf); // register raw frequency		
		text.addFeature("tf_doc_len_based", tfDocLenBased);
		text.addFeature("tf_max_rtf_based", tfMaxRtfBased);
				
	}
	
	/**
	 * Calculates the Inverse sentence Frequency (isf) for each word.
	 * 		
	 * 	isf = log(N/n)
	 * 	N total number of sentences in the text
	 *	n number of sentences that contains the term
	 *  
	 */
	public void isf(Text text) {
		
		log.debug("Calculating ISF (inverse sentence frequency).");
		
		// inverted index used for count sentences with term occurrence
		HashMap<String, HashSet<Integer>> index = new HashMap<>(); 
		int sentenceCounter = 0;
		
		for (Paragraph p : text.getParagraphs()) {
			for (Sentence s : p.getSentences()) {
				// increment sentence counter
				sentenceCounter++;
				s.getWords().forEach(w -> {
					String key = w.getCurrentValue();
					if(index.containsKey(key)) {
						index.get(key).add(s.getId());
					} else {
						index.put(key, new HashSet<Integer>());
						index.get(key).add(s.getId());
					}
				});
			}
		}
		
		Map<String, Double> isf = new HashMap<>();
		for (Entry<String, HashSet<Integer>> e : index.entrySet()) {
			isf.put(e.getKey(), Math.log10((double)sentenceCounter / (double)e.getValue().size()));
		}
		
		text.addFeature("sentence_counter", sentenceCounter);
		text.addFeature("isf", isf);
		
	}
	
	/**
	 * Term frequency Inverse sentence frequency. Calculates the TF-ISF for each word and sentence of the text. 
	 * 
	 * tf_isf = tf x isf
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Text tfIsf(Text text) {
		
		log.debug("Calculating TF-ISF (term frequency, inverse sentence frequency.");
		
		HashMap<String, Integer> rtf = (HashMap<String, Integer>) text.getFeature("rtf");
		HashMap<String, Double> isf = (HashMap<String, Double>) text.getFeature("isf");
		
		double maxRes = 0.0;
		
		Map<String, Double> tfisf = new HashMap<>();
		for (Entry<String, Integer> e : rtf.entrySet()) {
			Double res = (double)e.getValue() * isf.get(e.getKey());
			if(res > maxRes) maxRes = res;
			tfisf.put(e.getKey(), res);
		}
		
		for (String key : tfisf.keySet()) {
			tfisf.put(key, tfisf.get(key) / maxRes); // tfisf normalization
		}
		
		// calculates the sentence tfisf
		double sentenceTfIsf;
		maxRes = .0;
		for (Paragraph p : text.getParagraphs()) {
			for (Sentence s : p.getSentences()) {
				sentenceTfIsf = .0;
				for (Word w : s.getWords()) {
					sentenceTfIsf += tfisf.get(w.getCurrentValue());
				}
				if(sentenceTfIsf > maxRes) maxRes = sentenceTfIsf;
				s.addFeature("tf_isf", sentenceTfIsf);
			}
		}
		
		text.addFeature("tf_isf", tfisf);
	
		for (Paragraph p : text.getParagraphs()) {
			for (Sentence s : p.getSentences()) {
				// normalize tfisf
				s.addFeature("tf_isf", (double)s.getFeature("tf_isf") / maxRes);
				// set tfisf for each word
				for (Word w : s.getWords()) {
					w.addFeature("tf_isf", tfisf.get(w.getCurrentValue()));
				}
			}
		}
		return text;
	}
	
	@Override
	public String toString() {
		return "Frequency";
	}

	@Override
	public Text process(Text text) {
		log.debug("Calculating frequency features for " + text.getName());
		this.tf(text);
		this.isf(text);
		return tfIsf(text);
	}
	
}
