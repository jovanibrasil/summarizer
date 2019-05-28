package summ.nlp.features;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Text;
import summ.utils.Pipe;

public class Similarity implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(Similarity.class);
	
    /**
     * Returns a set with strings common to the two given maps.
     *
     * @param leftVector left vector map
     * @param rightVector right vector map
     * @return common strings
     */
    private Set<CharSequence> getIntersection(final Map<CharSequence, Double> leftVector,
            final Map<CharSequence, Double> rightVector) {
        final Set<CharSequence> intersection = new HashSet<>(leftVector.keySet());
        intersection.retainAll(rightVector.keySet());
        return intersection;
    }

    /**
     * Computes the dot product of two vectors. It ignores remaining elements. It means
     * that if a vector is longer than other, then a smaller part of it will be used to compute
     * the dot product.
     *
     * @param leftVector left vector
     * @param rightVector right vector
     * @param intersection common elements
     * @return the dot product
     */
    private double dot(final Map<CharSequence, Double> leftVector, final Map<CharSequence, Double> rightVector,
            final Set<CharSequence> intersection) {
        long dotProduct = 0;
        for (final CharSequence key : intersection) {
            dotProduct += leftVector.get(key) * rightVector.get(key);
        }
        return dotProduct;
    }
	
	/**
	 * Calculates the cosine similarity between two vectors of double values. The
	 * two vectors must have the same size.
	 * 
	 * Similarity = cosine(angle) = (A . B)/(||A|| . ||b||) 
	 * 
	 * @param a is a vector of double values
	 * @param b is a vector of double values
	 * @return a cosine similarity vector between the vectors
	 * 
	 */
	public Double calculateSimilarity(final Map<CharSequence, Double> a, final Map<CharSequence, Double> b) {
		if (a == null || b == null) {
            throw new IllegalArgumentException("Vectors must not be null");
        }
		
		double dotProduct = 0.0, d1 = 0.0, d2 = 0.0;
		
        final Set<CharSequence> intersection = getIntersection(a, b);
        dotProduct = dot(a, b, intersection);

		for (final Double value : a.values()) {
			 d1 += Math.pow(value, 2);
        }
        for (final Double value : b.values()) {
        	d2 += Math.pow(value, 2);
        }
		
		double similarity = 0.0;
		
		if(d1 >= 0 && d2 >= 0) {
			similarity = dotProduct / 
					( Math.sqrt(d1) * Math.sqrt(d2) );	
		}
		
		return similarity;
	}	
	
	/**
	 * Calculates cosine similarity between two vectors of strings.
	 * 
	 * @param leftVector
	 * @param rightVector
	 * @param implType
	 * @return
	 * 
	 */
	public Double calculateCharSequenceSimilarity(Map<CharSequence, Double> leftVector, 
			Map<CharSequence, Double> rightVector) {
			return calculateSimilarity(rightVector, leftVector);
	}
	
	public Double calculateApacheCharSequenceSimilarity(Map<CharSequence, Integer> leftVector, 
			Map<CharSequence, Integer> rightVector) {
			CosineSimilarity cosineSimilarity = new CosineSimilarity();
			return cosineSimilarity.cosineSimilarity(leftVector, rightVector);	
	}

	@Override
	public String toString() {
		return "Text similarity";
	}
	
	@Override
	public Text process(Text text) {
//		double ranks[] = this.calculateTextRank(text, null);
//		for (Sentence sentence : text.getSentences()) {
//			sentence.addFeature("text-rank", ranks[sentence.getId()]);
//		}
		return null;
	}


}
