package summ.utils;

import org.apache.log4j.Logger;

import summ.model.Text;
import summ.nlp.features.FeatureFactory;
import summ.nlp.features.FeatureType;
import summ.nlp.preprocesing.PreProcessingTypes;
import summ.nlp.preprocesing.PreprocessingFactory;

public class PipelineFactory {

	static Logger log = Logger.getLogger(PipelineFactory.class);
	
	public static Pipeline<Text> getPreprocessingPipeline(String[] val){
		Pipeline<Text> pipeline = new Pipeline<Text>();
		for (String string : val) {
			PreProcessingTypes preprocessingType = PreProcessingTypes.valueOf(string);
			if(preprocessingType != null) {
				pipeline.addPipe(PreprocessingFactory.getPreprocessing(preprocessingType));
			}else {
				log.info("Skipped invalid preprocessing method " + string);
			}
		}
		return pipeline;
	}
	
	public static Pipeline<Text> getFeaturesPipeline(String[] featuresList){
		Pipeline<Text> pipeline = new Pipeline<Text>();
		for (String string : featuresList) {
			FeatureType featureType = FeatureType.valueOf(string);
			if(featureType != null) {
				pipeline.addPipe(FeatureFactory.getFeature(featureType));
			}else {
				log.info("Skipped invalid feature " + string);
			}
		}
		return pipeline;
	}
	
}
