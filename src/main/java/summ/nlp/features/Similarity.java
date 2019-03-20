package summ.nlp.features;

import java.util.Map;

import org.apache.commons.text.similarity.CosineSimilarity;

public class Similarity {

	/*
	 * Calculate cosine similarity between two vectors of double values. The
	 * two vectors must have the same size.
	 * 
	 * Similarity = cosine(angle) = (A . B)/(||A|| . ||b||) 
	 * 
	 * @param a is a vector of double values
	 * @param b is a vector of double values
	 * @return similarity between the vectors
	 * 
	 */
	public static Double calculateSimilarity(double a[], double b[]) {
		// TODO throw exception when different vectors size
		
		double numeratorSummation = 0.0;
		double a1PowerSummation = 0.0;
		double a2PowerSummation = 0.0;
		
		for (int i = 0; i < b.length; i++) {
			numeratorSummation += a[i] * b[i]; 
			a1PowerSummation += Math.pow(a[i], 2);
			a2PowerSummation += Math.pow(b[i], 2);
		}
		
		double similarity = numeratorSummation / 
				( Math.sqrt(a1PowerSummation) * Math.sqrt(a2PowerSummation) );
		
		return similarity;
	}
	
	/*
	 * Calculate cosine similarity between two vectors of strings.
	 * 
	 * @param leftVector
	 * @param rightVector
	 * @param implType
	 * @return
	 * 
	 */
	public static Double calculateCharSequenceSimilarity(Map<CharSequence, Integer> leftVector, 
			Map<CharSequence, Integer> rightVector, ImplementationType implType) {
		
		if(implType.equals(ImplementationType.APACHE_COMMONS)) {
			CosineSimilarity cosineSimilarity = new CosineSimilarity();
			return cosineSimilarity.cosineSimilarity(leftVector, rightVector);	
		}else {
			//return calculateSimilarity(a.values().toArray(), b.values().toArray());
			// TODO como tratar sentenças com tamanhos diferentes? HOW?
			return 0.0;
		}
		
	}
	
//	String s1 = "..."; String s2 = "...";
//    Map<CharSequence, Integer> leftVector =
//	        Arrays.stream(s1.split(""))
//	        .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
//	    Map<CharSequence, Integer> rightVector =
//	        Arrays.stream(s2.split(""))
//	        .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));

	

}
