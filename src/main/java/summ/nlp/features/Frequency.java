package summ.nlp.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;
import summ.utils.Pipe;

public class Frequency implements Pipe<Text> {
	
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
		
		HashMap<String, Integer> rtf = new HashMap<String, Integer>(); 
		int count = 0; String maxRtfKey = ""; 
		rtf.put(maxRtfKey, 0);
		for (Paragraph p : text.getParagraphs()) {
			for (Sentence s : p.getSentences()) {
				for (Word w : s.getWords()) {
					// increment word counter
					count++;
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
			tfDocLenBased.put(e.getKey(), (double)e.getValue() / count);
		}
		
		
		Map<String, Double> tfMaxRtfBased = new HashMap<>();
		for (Entry<String, Integer> e : rtf.entrySet()) {
			tfMaxRtfBased.put(e.getKey(), (double)e.getValue() / rtf.get(maxRtfKey));
		}
		
		
		text.addFeature("doc-length", count); // 
		text.addFeature("rtf", rtf); // register raw frequency		
		text.addFeature("tf-doc-len-based", tfDocLenBased);
		text.addFeature("tf-max-rtf-based", tfMaxRtfBased);
		
	}
	
	/**
	 * Inverse sentence Frequency
	 * 		
	 * 		isf = log(N/n)
	 * 		N número de sentenças do documento
	 *		n número de sentenças que contém o termo
	 *  
	 */
	public void isf(Text text) {
		
		// inverted index used for count sentences with term occurrence
		HashMap<String, HashSet<Integer>> index = new HashMap<>(); 
		int count = 0;
		
		for (Paragraph p : text.getParagraphs()) {
			for (Sentence s : p.getSentences()) {
				// increment sentence counter
				count++;
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
		
		// log (matemtica)  log 10 em java -  o inverso de 10^x
		// ln (matemtica)  a funo log em java que  log_e x ou log x  
		// 	-  o inverno de e^x e tabm chamado de logaritmo natural
		
		Map<String, Double> isf = new HashMap<>();
		for (Entry<String, HashSet<Integer>> e : index.entrySet()) {
			isf.put(e.getKey(), Math.log10((double)count / (double)e.getValue().size()));
		}
		
		text.addFeature("sentence-counter", count);
		text.addFeature("isf", isf);
		
	}
	
	/**
	 * Term frequency Inverse sentence frequency. Calcula o TF-ISF para cada palavra e sentença 
	 * do texto. Esta implementação considera apenas a análise de texto mono-documento.
	 * 
	 * 		tf_isf = tf x isf
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Text tfIsf(Text text) {
		
		HashMap<String, Integer> rtf = (HashMap<String, Integer>) text.getFeature("rtf");
		HashMap<String, Double> isf = (HashMap<String, Double>) text.getFeature("isf");
		
		double maxRes = 0.0;
		
		Map<String, Double> tfisf = new HashMap<>();
		for (Entry<String, Integer> e : rtf.entrySet()) {
			Double res = (double)e.getValue() * isf.get(e.getKey());
			if(res > maxRes)
				maxRes = res;
			tfisf.put(e.getKey(), res);
		}
		
		for (String key : tfisf.keySet()) {
			tfisf.put(key, tfisf.get(key) / maxRes);
			//System.out.println(key + " tf-isf = " + tfisf.get(key) + " tf = " + rtf.get(key));	
		}
		
		// calculates the sentence tfisf 
		for (Paragraph p : text.getParagraphs()) {
			for (Sentence s : p.getSentences()) {
				maxRes = .0;
				for (Word w : s.getWords()) {
					maxRes += tfisf.get(w.getCurrentValue());
				}
				s.addFeature("tf-isf", maxRes / s.getLength());
			}
		}
		
		text.addFeature("tf-isf", tfisf);
	
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				// increment sentence counter
				s.getWords().forEach(w -> {
					w.addFeature("tf-isf", tfisf.get(w.getCurrentValue()));
				});
			});
		});
		return text;
	}

	@Override
	public Text process(Text text) {
		this.tf(text);
		this.isf(text);
		return tfIsf(text);
	}
	
}
