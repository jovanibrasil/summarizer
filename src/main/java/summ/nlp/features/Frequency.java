package summ.nlp.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import summ.model.Text;
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
		var wrapper = new Object() { int count = 0; String maxRtfKey = ""; };
		rtf.put(wrapper.maxRtfKey, 0);
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				s.getWords().forEach(w -> {
					// increment word counter
					wrapper.count++;
					// increment frequency counter
					String key = w.getRawWord();
					int value = rtf.containsKey(key) ? rtf.get(key) + 1 : 1;
					rtf.put(key, value);
					// update max frequency
					wrapper.maxRtfKey = rtf.get(key) > rtf.get(wrapper.maxRtfKey) ? key : wrapper.maxRtfKey; 
				});
			});
		});
		rtf.remove("");
		Map<String, Double> tfDocLenBased = rtf.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> (double)e.getValue() / wrapper.count));
		
		Map<String, Double> tfMaxRtfBased = rtf.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> (double)e.getValue() / rtf.get(wrapper.maxRtfKey)));
		
		text.addFeature("doc-length", wrapper.count); // 
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
		var wrapper = new Object() { int count = 0; };
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				// increment sentence counter
				wrapper.count++;
				s.getWords().forEach(w -> {
					String key = w.getRawWord();
					
					if(index.containsKey(key)) {
						index.get(key).add(s.getPos());
					} else {
						HashSet<Integer> hs = new HashSet<Integer>();
						hs.add(s.getPos());
						index.put(key, hs);
					}
					
				});
			});
		});
		
		Map<String, Double> isf = index.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(),
						e -> Math.log((double)wrapper.count / e.getValue().size())));
		
		text.addFeature("sentence-counter", wrapper.count);
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
		
		var wrapper = new Object() { Double maxRes = 0.0; };
		
		Map<String, Double> tfisf = rtf.entrySet().stream()
			.collect(Collectors.toMap(e -> e.getKey(), e -> {
				Double res = (double)e.getValue() / isf.get(e.getKey());
				if(res > wrapper.maxRes)
					wrapper.maxRes = res;
				return res;
			}));
		
		tfisf.keySet().forEach(key -> {
			tfisf.put(key, tfisf.get(key) / wrapper.maxRes);
			//System.out.println(key + " tf-isf = " + tfisf.get(key) + " tf = " + rtf.get(key));
		});
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				wrapper.maxRes = .0;
				s.getWords().forEach(w -> {
					wrapper.maxRes += tfisf.get(w.getRawWord());
				});
				s.addFeature("tf-isf", wrapper.maxRes / s.getLength());
			});
		});
		
		text.addFeature("tf-isf", tfisf);
	
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				// increment sentence counter
				s.getWords().forEach(w -> {
					w.addFeature("tf-isf", tfisf.get(w.getRawWord()));
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
