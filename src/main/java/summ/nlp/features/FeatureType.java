package summ.nlp.features;

public enum FeatureType {
	TFIDF, // Term frequency inverse document frequency 
	TFISF, // Term frequency inverse sentence frequency 
	TF, // Term frequency
	LOCATION, 
	LENGTH, 
	LOC_LEN, // Location and length correlation
	ISF,// Inverse sentence frequency
	TITLE_WORDS, 
	FREQUENCY, 
	TEXTRANK
}
