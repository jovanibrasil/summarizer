package summ.nlp.features;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.model.Sentence;
import summ.model.Text;
import summ.utils.Pipe;

/**
 * Author: Jovani Brasil
 */
public class TextRank implements Pipe<Text> {

	private static final Logger log = LogManager.getLogger(TextRank.class);
	
	private FeatureType featureType;
	private Similarity similarity;
	
	public TextRank(FeatureType featureType) {
		this.featureType = featureType;
		this.similarity = new Similarity();
	}
	
	/**
	 * Get a map of pairs (word, feature).
	 * 
	 * @param sentence is a sentence object.
	 * @param featureType is the type of the feature (tf-idf, lex-chain).
	 * 
	 * @return a map of pairs (word, feature).
	 * 
	 */
	private Map<CharSequence, Double> getVector(Sentence sentence, FeatureType featureType){
		Map<CharSequence, Double> vector = new HashMap<>();
		sentence.getWords().forEach(w -> {
        	vector.put(w.getCurrentValue(), w.getFeature("tf-isf"));
        });
	    return vector;
	}
	
	/**
	 * Calculate similarity matrix based on text object passed in the object initialization.
	 *
	 * @param text is the text that will be processed. The text must contains at least one feature, that will be used
	 * in the vector creation. This feature must be stored in the word.
	 *
	 * @return a similarity matrix.
	 *
	 */
	public double[][] calculateSimilarityMatrix(Text text) {
		double weight = 0;
		Sentence[] sentences =  text.getSentences();
		int sentencesLen = sentences.length;
		double similarityMatrix[][] = new double[sentencesLen][sentencesLen];
		
		for (int i = 0; i < sentencesLen; i++) {
			for (int j = 0; j < sentencesLen; j++) {
				if(i != j) {
					weight = similarity.calculateCharSequenceSimilarity(getVector(sentences[i], featureType), 
						getVector(sentences[j], featureType));
				}
				similarityMatrix[i][j] = weight;
				weight = 0;
			}
		}
		return similarityMatrix;
	}

	/**
	 * Calculate the rank value for every sentence of the text.
	 *
	 * @param text is the text that will be processed. The text must contains at least one feature, that will be used
	 * in the vector creation. This feature must be stored in the word.
	 *
	 * @return a list of sentence rank values.
	 *
	 */
	public double[] calculateSentenceRank(Text text, double[][] similarityMatrix) {
		Sentence[] sentences =  text.getSentences();
		int sentencesLen = sentences.length;
		// SES = (Somatório das similaridades) / MAX(Somatório das similaridades)
		double[] rank = new double[sentencesLen];
		double maxRank = 0.0;
		for (int i = 0; i < sentencesLen; i++) {
			double summation = 0.0;
			for (int j = 0; j < sentencesLen; j++) {
				summation += similarityMatrix[i][j];
			}
			rank[i] = summation;
			if(summation > maxRank) {
				maxRank = summation;
			}
		}
		for (int i = 0; i < rank.length; i++) {
			rank[i] /= maxRank; 
		}
		return rank;
	}

	/**
	 * Calculate the rank for all text sentences.
	 * 
	 * @return a list of sentence rank values.
	 * 
	 */
	public double[] calculateTextRank(Text text, FeatureType featureType) {
		double[][] similarityMatrix = this.calculateSimilarityMatrix(text);
		double[] rankBySentence = this.calculateSentenceRank(text, similarityMatrix);
		return rankBySentence;
	}

	@Override
	public Text process(Text text) {
		double ranks[] = this.calculateTextRank(text, null);
		for (Sentence sentence : text.getSentences()) {
			sentence.addFeature("text-rank", ranks[sentence.getId()]);
		}
		return text;
	}
	
}
