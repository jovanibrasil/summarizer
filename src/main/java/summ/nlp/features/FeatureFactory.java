package summ.nlp.features;

import summ.model.Text;
import summ.utils.Pipe;

public class FeatureFactory {

	public static Pipe<Text> getFeature(FeatureType featureType){
		switch (featureType) {
			case FREQUENCY:
				return new Frequency();
			case ISF: 
				return new Frequency();
			case LENGTH:
				return new Length();
			case LOCATION:
				return new Location();
			case LOC_LEN:
				return new LocLen();
			case TF:
				return new Frequency();
			case TFIDF:
				return new Frequency();
			case TFISF:
				return new Frequency();
			case TITLE_WORDS:
				return new Title(); 
			case TEXTRANK:
				return new TextRank(FeatureType.TFIDF);
			default:
				return null;
		}
	}
	
}
