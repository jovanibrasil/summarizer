package com.nlp.features;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import summ.nlp.features.ImplementationType;
import summ.nlp.features.Similarity;

class SimilarityTest {

	private static final double DELTA = 1e-6;
	
	public static Map<CharSequence, Double> convertToMap(double values[]){
		return new HashMap<>() {{
	        for (int i = 0; i < values.length; i++) {
	        	put("word"+i, values[i]);	
			}
	    }};
	}
	
	@Test
	void testSenteceCustomCosineSimilarity() {
		
		double a[] = { 3.0, 8.0, 7.0, 5.0, 2.0, 9.0 }; // 6-dimensional vector
		double b[] = { 10.0, 8.0, 6.0, 6.0, 4.0, 5.0 };
	    
		double result = Similarity.calculateCharSequenceSimilarity(convertToMap(a), convertToMap(b));
		assertEquals(0.8638935626791596, result, DELTA);
		
		a = new double[] { 1, 0, -1 }; // 3-dimensional vector
		b = new double[] { -1,-1, 0 };
		
		result = Similarity.calculateCharSequenceSimilarity(convertToMap(a), convertToMap(b));
		assertEquals(-0.5, result, DELTA);
		
		a = new double[] { 3, 45, 7, 2 }; // 4-dimensional vector
		b = new double[] { 2, 54, 13, 15 };
		
		result = Similarity.calculateCharSequenceSimilarity(convertToMap(a), convertToMap(b));
		assertEquals(0.972284251712, result, DELTA);
		
		a = new double[] { 1,2,3 };
		b = new double[] { 1,1,4 };
		
		result = Similarity.calculateCharSequenceSimilarity(convertToMap(a), convertToMap(b));
		assertEquals(0.9449111825230682, result, DELTA);
		
		a = new double[] { 2, 1, 0, 2, 0, 1, 1, 1 }; // 8-dimensional vector
		b = new double[] { 2, 1, 1, 1, 1, 0, 1, 1 };
		
		result = Similarity.calculateCharSequenceSimilarity(convertToMap(a), convertToMap(b));
		assertEquals(0.821583, result, DELTA);
		
		a = new double[] { 1, 1, 1, 1, 1, 1, 1, 1 }; // 8-dimensional vector
		b = new double[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		
		result = Similarity.calculateCharSequenceSimilarity(convertToMap(a), convertToMap(b));
		assertEquals(1.0, result, DELTA);
		
	}
	
	@Test
	void testTotallyDifferentSentecesSimilarity() {
		
		Map<CharSequence, Double> leftVector = new HashMap<>() {{
	        put("Hello", 3.0); put("this", 8.0); put("is", 7.0);
	        put("a", 5.0); put("method", 2.0); put("test", 9.0);
	    }};
	    
	    Map<CharSequence, Double> rightVector = new HashMap<>() {{
	        put("Good", 10.0); put("morning", 8.0); put("my", 6.0); put("friend", 6.0);
	    }};
	    
	    double result = Similarity.calculateCharSequenceSimilarity(leftVector, rightVector);
		assertEquals(0.0, result, DELTA);
		
	}
	
	@Test
	void testSenteceApacheCosineSimilarity() {
		
		Map<CharSequence, Integer> leftVector = new HashMap<>() {{
	        put("Hello", 3); put("this", 8); put("is", 7);
	        put("a", 5); put("method", 2); put("test", 9);
	    }};
	    
	    Map<CharSequence, Integer> rightVector = new HashMap<>() {{
	        put("Hello", 10); put("this", 8); put("is", 6);
	        put("a", 6); put("method", 4); put("test", 5);
	    }};
	    
		double result = Similarity.calculateApacheCharSequenceSimilarity(leftVector, rightVector);
		assertEquals(0.8638935626791596, result);
		
	}
	
	@Test
	void testSenteceCosineSimilarityDifferentVectorsSize() {
		
		Map<CharSequence, Integer> leftVector = new HashMap<>() {{
	        put("Hello", 3); put("this", 8); put("is", 7);
	        put("a", 5); put("method", 2);
	    }};
	    
	    Map<CharSequence, Integer> rightVector = new HashMap<>() {{
	        put("Hello", 10); put("this", 8); put("is", 7);
	        put("a", 5);
	    }};
	    
		Assertions.assertDoesNotThrow(() -> {
			Similarity.calculateApacheCharSequenceSimilarity(leftVector, rightVector);
		});
		
	}

}
