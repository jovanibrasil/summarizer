package summ.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import com.opencsv.CSVWriter;

import summ.model.Sentence;
import summ.model.Text;

public class ExportCSV {

	@SuppressWarnings("unchecked")
	/**
	 * Saves the features data in as a CSV file.
	 * 
	 * @param text is a Text object that contains the data that will be saved.
	 */
	public static void exportFeaturesValues(Text text) {
		try {
			File file = new File("results/texts-evaluation-features" + (new Date()).toString() + ".csv") ;
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);			
			String[] header = null;
			// Export all sentence features for each sentence
			Sentence[] sentences = text.getSentences();
			for (int x=0; x<sentences.length; x++) {
				Sentence sentence = sentences[x];
				if(header == null) {
					header = new String[sentence.getFeatures().size()+1];
					header[0] = "Sentence ID";
					int headerIndex = 1;
					for (String headerItem : sentence.getFeatures().keySet()) {
						header[headerIndex++] = headerItem;
					}
					writer.writeNext(header);
				}
				String[] data = new String[header.length+1];
				data[0] = String.valueOf(sentence.getId());
				for (int i = 0; i < header.length-1; i++) {
					data[i+1] = sentence.getFeature(header[i+1]).toString(); 
				}
				writer.writeNext(data);
			}
			// Export all general features
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
	
}
