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

class ConsineSimilarityTest {

	@Test
	void testCalculateCosineSimilarity() {
		double a[] = {3.0, 8.0, 7.0, 5.0, 2.0, 9.0};
		double b[] = {10.0, 8.0, 6.0, 6.0, 4.0, 5.0};
		double similarity = Similarity.calculateSimilarity(a, b);
		assertEquals(0.8638935626791596, similarity);
	}
	
	@Test
	void testSenteceCosineSimilarity() {
		
		Map<CharSequence, Integer> leftVector = new HashMap<>() {{
	        put("Hello", 3); put("this", 8); put("is", 7);
	        put("a", 5); put("method", 2); put("test", 9);
	    }};
	    
	    Map<CharSequence, Integer> rightVector = new HashMap<>() {{
	        put("Hello", 10); put("this", 8); put("is", 6);
	        put("a", 6); put("method", 4); put("test", 5);
	    }};
	    
		double result = Similarity.calculateCharSequenceSimilarity(leftVector, rightVector, ImplementationType.APACHE_COMMONS);
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
			Similarity.calculateCharSequenceSimilarity(leftVector, rightVector, ImplementationType.APACHE_COMMONS);
		});
		
	}

}
