package summ.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.opencsv.CSVWriter;

import summ.model.Sentence;
import summ.model.Text;
import summ.nlp.evaluation.EvaluationResult;
import summ.nlp.evaluation.EvaluationTypes;

public class ExportCSV {

	/**
	 * Saves the sentences features data as a CSV file.
	 * 
	 * @param text is a Text object that contains the data that will be saved.
	 */
	public static void exportSentenceFeatures(Text text, String outputPath, String fileName) {
		try {
			File file = new File(outputPath + "/"+ fileName +"texts-evaluation-features" + 
					Utils.generateStringFormattedData() + ".csv") ;
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);			
			String[] header = null;
			// Export all sentence features for each sentence
			Sentence[] sentences = text.getSentences();
			for (int x=0; x<sentences.length; x++) {
				Sentence sentence = sentences[x];
				if(header == null) {
					header = new String[sentence.getFeatures().size()+2];
					header[0] = "Sentence ID";
					header[1] = "Score";
					int headerIndex = 2;
					for (String headerItem : sentence.getFeatures().keySet()) {
						header[headerIndex++] = headerItem;
					}
					writer.writeNext(header);
				}
				String[] data = new String[header.length+1];
				data[0] = String.valueOf(sentence.getId());
				data[1] = String.valueOf(sentence.getScore());
				for (int i = 0; i < header.length-2; i++) {
					data[i+2] = sentence.getFeature(header[i+2]).toString(); 
				}
				writer.writeNext(data);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the word features data as a CSV File.
	 * 
	 * @param text
	 */
	@SuppressWarnings("unchecked")
	public static void exportWordFeatures(Text text, String outputPath) {
		try {
			File file = new File(outputPath + "/words-evaluation-features" + Utils.generateStringFormattedData() + ".csv") ;
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);			
			for (Entry<String, Object> entry : text.getFeatures().entrySet()) {
				if(entry.getValue() instanceof Double) {
					writer.writeNext(new String[] { entry.getKey(), entry.getValue().toString() });
				}else if(entry.getValue() instanceof Map) {
					writer.writeNext(new String[] { entry.getKey() });
					for (Entry<String, Object> feature : ((Map<String, Object>)entry.getValue()).entrySet()) {
						writer.writeNext(new String[] { feature.getKey(), feature.getValue().toString() });
					}
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveEvaluationResult(List<EvaluationResult> results, String outputPath) {
		
		try {
			File file = new File(outputPath + "/texts-evaluation-"+ Utils.generateStringFormattedData() + ".csv");  
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);

	        String[] header = new String[] { "textName", "precision", "recall", "fMeasure", "retrievedSentences", "relevantSentences", "correctSentences" };
	        writer.writeNext(header);
	        
			for (EvaluationResult result : results) {
				String[] data = { result.getEvalName(), 
						result.getMetricValue(EvaluationTypes.PRECISION.name()).toString(), 
						result.getMetricValue(EvaluationTypes.RECALL.name()).toString(), 
						result.getMetricValue(EvaluationTypes.FMEASURE.name()).toString(), 
						result.getMetricValue("retrievedSentences").toString(), 
						result.getMetricValue("relevantSentences").toString(),
						result.getMetricValue("correctSentences").toString() };
				writer.writeNext(data);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void exportOptimizationResults(List<List<Double>> results, String[] fileNames, String outputPath) {
		
		try {
			File file = new File(outputPath + "/OptimizationResult.csv");  
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	        String[] header = new String[fileNames.length];
	        header[0] = "Iteration / Configuration";
	        for (int i = 1; i < fileNames.length; i++) {
				header[i] = fileNames[i-1];
			}
	        writer.writeNext(header);
	        for (int j=0; j < results.size(); j++) {
	        	List<Double> result = results.get(j);
				String[] data = new String[result.size()+1];
				data[0] = String.valueOf(j); // iteration index
				for (int i = 1; i < result.size()+1; i++) {
					data[i] = String.valueOf(result.get(i-1)); // iteration results
				}
				writer.writeNext(data);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
