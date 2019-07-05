package summ.nlp.preprocesing;

public enum PreProcessingTypes {
	SENTENCE_SEGMENTATION, 
	REMOVE_PUNCTUATION,
	REMOVE_NUMBERS,
	REMOVE_DATES,
	REMOVE_MONEY,
	REMOVE_EMPTY_WORDS,
	TO_LOWER_CASE, 
	SIMPLE_TOKENIZATION,
	WHITE_SPACE_TOKENIZATION,
	ME_TOKENIZATION, // Maximum entropy tokenization
	REMOVE_STOPWORDS,
	NER, // Named EntityRecognition
	POS, // Part-Of-Speech
	IDENTIFY_TITLES,
	LEMMATIZATION
}
