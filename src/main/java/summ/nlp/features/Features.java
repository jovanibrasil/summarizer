package summ.nlp.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import summ.model.Sentence;
import summ.model.Text;
import summ.model.Word;

public class Features {

	public Features(Text text) {
		
		this.location(text);
		this.length(text);
		this.locLen(text);
		this.tf(text);
		this.isf(text);
		this.tfIsf(text);
		this.titleWords(text);
		
		//System.out.println(text);
		
	}
	
	/*
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
		
		
//		tfDocLenBased.keySet().forEach(key -> {
//		
//			System.out.println(key + "rtf = " + rtf.get(key) + " tfDocLenBased = " 
//					+ tfDocLenBased.get(key) + "	 tfMaxRtfBased = " + tfMaxRtfBased.get(key));
//			
//			
//		});
//		
		
		text.addFeature("doc-length", wrapper.count); // 
		text.addFeature("rtf", rtf); // register raw frequency		
		text.addFeature("tf-doc-len-based", tfDocLenBased);
		text.addFeature("tf-max-rtf-based", tfMaxRtfBased);
		
	}
	
	/*
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
	
	/*
	 * Term frequency Inverse sentence frequency. Calcula o TF-ISF para cada palavra e sentença 
	 * do texto. Esta implementação considera apenas a análise de texto mono-documento.
	 * 
	 * 		tf_isf = tf x isf
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void tfIsf(Text text) {
		
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
	
	}
	
	/*
	 * Simple sentence location and the relative sentence location.
	 *
	 * Razão entre posição no parágrafo e tamanho da sentença
	 *
	 */
	public void location(Text text) {
	
		text.getParagraphs().forEach(p -> {
			int middle = p.getLength() / 2;
			p.getSentences().forEach(s -> {
				
				Double pos = (double)p.getLength() - s.getPos();
				
				Double simpleLocation = pos / p.getLength();
				s.addFeature("simple-location", simpleLocation);
				
				Double inverseLocation = 1 - (pos-1) / p.getLength();
				s.addFeature("inverse-location", inverseLocation); 
				
				Double relativeLocation = s.getPos() <= middle ? pos / p.getLength() 
						:  1 - ((pos - 1) / p.getLength());
				s.addFeature("relative-location", relativeLocation); 
				
			});
		});
		
	}
	
	/*
	 * Sentence length
	 * 
	 * Simple length is the sentence length divided by the length of the 
	 * longest sentence of the text.
	 * 
	 * Calcula a feature comprimento da sentença.
	 *
     *  len = ln(tla - | (tla - tl) / tal |)
	 *
     * Cada parágrafo é quebrado em dois e analisado individualmente.
	 *
     *  tla - média da quantidade de termos das sentenças
     *  tl - quantidade de termos de uma sentença
     *  tal - desvio padrão
	 * 
	 */
	public void length(Text text) {
		
		var wrapper = new Object() { int maxLength = 0; Double maxRelativeLength = 0.0; };
		
		text.getParagraphs().forEach(p -> {
			int middle = p.getLength() / 2;
			
			DescriptiveStatistics ds1 = new DescriptiveStatistics();
			DescriptiveStatistics ds2 = new DescriptiveStatistics();
			
			p.getSentences().forEach(s -> {
				wrapper.maxLength = s.getEditedSentenceLength() > wrapper.maxLength
						? s.getEditedSentenceLength() : wrapper.maxLength;	
				if(s.getPos() <= middle) {
					ds1.addValue(s.getEditedSentenceLength());
				} else {
					ds2.addValue(s.getEditedSentenceLength());
				}		
			});
			
			p.addFeature("tla1", ds1.getMean());
			p.addFeature("tla2", ds2.getMean());
			p.addFeature("tal1", ds1.getStandardDeviation());
			p.addFeature("tal2", ds2.getStandardDeviation());
			
		});
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				int middle = p.getLength() / 2;
				
				Double tla, tal;
				int tl = s.getEditedSentenceLength();
				
				if(s.getPos() <= middle) {
					tla = p.getFeature("tla1"); tal = p.getFeature("tal1");
				} else {
					tla = p.getFeature("tla2"); tal = p.getFeature("tal2");
				}
				
				tal = tal == 0 ? tla : tal;
				tal = tal == 0 ? 1 : tal;
				
				Double relativeLen = Math.log(tla - Math.abs((tla - tl) / tal));
				
				if(relativeLen > wrapper.maxRelativeLength) {
					wrapper.maxRelativeLength = relativeLen;
				}
				
				s.addFeature("len", s.getLength());
				s.addFeature("simple-len", (double)s.getEditedSentenceLength() / wrapper.maxLength);
				s.addFeature("relative-len", relativeLen);
			});
		});
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				s.addFeature("relative-len", (double)s.getFeature("relative-len") / wrapper.maxRelativeLength);
			});
		});
				
	}
	
	/*
	 * Correlation between location and length. 
	 * 
	 */
	public void locLen(Text text) {
		
		var wrapper = new Object() { Double maxLocLen = 0.0; };
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				Double loc = (Double)s.getFeature("relative-location");
				Double len = (Double)s.getFeature("relative-len");
				Double locLen = (-0.084 + (0.08 * len) + (2.344 * loc));
				s.addFeature("loc-len", locLen);
				wrapper.maxLocLen = locLen > wrapper.maxLocLen 
						? locLen : wrapper.maxLocLen;
			});
		});
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				s.addFeature("loc-len", (Double)s.getFeature("loc-len") / wrapper.maxLocLen);
			});
		});
				
	}
	
	/*
	 * Title words
	 * 
	 * This method ignores any title.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void titleWords(Text text) {
		
		Sentence title = text.getTitle();
		
		var wrapper = new Object() { int wordCount = 0; Double summation = 0.0; Double maxSummation = 1.0; };
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				
				wrapper.wordCount = 0; 
				wrapper.summation = 0.0;
			
				if(!s.isTitle()) {
				
					Map<String, Double> tfIsf = (Map<String, Double>) text.getFeature("tf-isf");
					s.getWords().forEach(w -> {
						if(title.containsWord(w)) {
							wrapper.wordCount++;
							Double weight = tfIsf.get(w.getRawWord());
							wrapper.summation += Math.pow(weight, 2);
						}
					});
					
					if(wrapper.summation > wrapper.maxSummation) {
						wrapper.maxSummation = wrapper.summation;
					}
				
				}
			
				s.addFeature("title-words-counter", wrapper.wordCount);
				s.addFeature("title-words-relative", wrapper.summation);
				
			});
		});
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				s.addFeature("title-words-relative", (Double)s.getFeature("title-words-relative") / wrapper.maxSummation);
			});
		});
				
	}
	
}
