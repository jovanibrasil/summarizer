package summ.summarizer;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.opencsv.CSVWriter;

import summ.ai.fuzzy.FuzzySystem;
import summ.ai.fuzzy.optimization.Optimization;
import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationTypes;
import summ.nlp.features.FeatureType;
import summ.nlp.features.Frequency;
import summ.nlp.features.Length;
import summ.nlp.features.LocLen;
import summ.nlp.features.Location;
import summ.nlp.features.TextRank;
import summ.nlp.features.Title;
import summ.nlp.preprocesing.General;
import summ.nlp.preprocesing.Lemmatizer;
import summ.nlp.preprocesing.POSTagger;
import summ.nlp.preprocesing.PreProcessingTypes;
import summ.nlp.preprocesing.SentenceSegmentation;
import summ.nlp.preprocesing.StopWords;
import summ.nlp.preprocesing.Titles;
import summ.nlp.preprocesing.Tokenization;
import summ.utils.ExportCSV;
import summ.utils.Pipeline;
import summ.utils.Tuple;
import summ.utils.Utils;

public class Summarizer {
	
	public static Pipeline<Text> getSummaryPreProcessingPipeline() {
		return new Pipeline<Text>(new SentenceSegmentation(), new Tokenization(PreProcessingTypes.NEURAL_TOKENIZATION));
	}

	public static Pipeline<Text> getTextPreProcessingPipeline() {
		return new Pipeline<Text>(new SentenceSegmentation(), new General(PreProcessingTypes.TO_LOWER_CASE),
				new General(PreProcessingTypes.REMOVE_PUNCTUATION), new Tokenization(PreProcessingTypes.NEURAL_TOKENIZATION),
				new Titles(), new StopWords(), new POSTagger(), new Lemmatizer(null));
	}
	
	public static Text featureComputation(Text text) {
		return new Pipeline<Text>(
				new Location(), new Length(), new LocLen(), new Frequency(), 
					new Title(), new TextRank(FeatureType.TFISF)).process(text);
		
	}

	public static ArrayList<Tuple<Integer, Double>> computeSentencesInformativity(Text text, FuzzySystem fs) {
		ArrayList<Tuple<Integer, Double>> outList = new ArrayList<Tuple<Integer, Double>>();
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {

				Tuple<String, Double> tfIsf = new Tuple<>("k1", (Double) s.getFeature("tf-isf"));
				Tuple<String, Double> locLen = new Tuple<>("loc_len", (Double) s.getFeature("loc-len"));
				Tuple<String, Double> titleWords = new Tuple<>("k2", (Double) s.getFeature("title-words-relative"));

				List<Tuple<String, Double>> inputVariables = new ArrayList<>(Arrays.asList(tfIsf, locLen, titleWords));
				Double out = fs.run(inputVariables, "informatividade");
				outList.add(new Tuple<>(s.getId(), out));
			});
		});
		Collections.sort(outList);
		return outList;
	}

	public static Text generateSummary(Text text, int summarySize, FuzzySystem fs) {
		// Compute sentences informativity using fuzzy system
		ArrayList<Tuple<Integer, Double>> outList = computeSentencesInformativity(text, fs);
		// Generate the summary
		Text generatedSummary = new Text("");
		Paragraph paragraph = new Paragraph(""); // TODO where is the full paragraph text? Is it necessary?
		int count = 0;
		for (Tuple<Integer, Double> t : outList) {
			Sentence sentence = text.getSentenceById(t.x);
			// Title sentences are ignored
			if (sentence.isTitle())
				continue;
			paragraph.addSentence(sentence);
			if (count == summarySize) {
				break;
			}
			count++;
		}
		generatedSummary.addParagraph(paragraph);
		return generatedSummary;
	}

	public static Text summarize(Text text, int summarySize) {

		// Pre-processing
		text = getTextPreProcessingPipeline().process(text);
		text = featureComputation(text);
		
		System.out.println(text);

		// Summary generation
		FuzzySystem fs = new FuzzySystem("flc/fb2015.flc");
		
		// int summarySize = (int)(0.3 * text.getTotalSentence());
		Text generatedSummary = generateSummary(text, summarySize, fs);
		
		return generatedSummary;
	}
	
	public static void summarizationText(String fileName) {
		// Load complete text
		Text text = Utils.loadText("projects/temario-2014/full-texts/", fileName);
		Text referenceSummary = Utils.loadText("projects/temario-2014/summaries/"
					+ "reference/automatic/", "po96fe09-b_areference1.txt");
		
		referenceSummary = getSummaryPreProcessingPipeline().process(referenceSummary);

		int summarySize = referenceSummary.getTotalSentence();
		summarize(text, summarySize);
		
		// Evaluation methods
//		return Evaluation.evaluate(generatedSummary, 
//				referenceSummary, EvaluationTypes.OVERLAP);
//		
		ExportCSV.exportFeaturesValues(text);
	}

	public static void summarizeTexts() {
		
		HashMap<String, Text> texts = Utils.loadTexts("projects/temario-2014/full-texts/", null);
		HashMap<String, Text> refSummaries = Utils.loadTexts("projects/temario-2014/summaries/"
				+ "reference/automatic/", null);
		
		try {
			
			File file = new File("results/texts-evaluation-"+(new Date()).toString());  
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
			
			for (Entry<String, Text> entry : texts.entrySet()) {
				
				System.out.println("Processing " + entry.getValue().getName() + " ...");
				
				Text referenceSummary = refSummaries.get(entry.getKey());
				Text originalText = entry.getValue();
				
				referenceSummary = getSummaryPreProcessingPipeline().process(referenceSummary);
				
				// Summarize
				int summarySize = referenceSummary.getTotalSentence();
				Text generatedSummary = summarize(originalText, summarySize);
				
				// Evaluate generated summary
				HashMap<String, HashMap<String, Object>> result = Evaluation.evaluate(generatedSummary,
						referenceSummary, EvaluationTypes.OVERLAP);	
				
				// Save summarization result
				String[] data = { entry.getValue().getName(), result.get("result").get("total_sentences").toString(),
						result.get("result").get("overlap").toString() };
				writer.writeNext(data);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static Text getTextProcessedText(String filePath, String fileName) {
		// Load complete text
		Text text = Utils.loadText(filePath, fileName);
		// Pre-processing
		text = getTextPreProcessingPipeline().process(text);
		text = featureComputation(text);		
		return text;
	}
	
	public static void fuzzyOptimization() {

		Text text =  getTextProcessedText("projects/temario-2014/full-texts/", "ce94ab10-a.txt");

		// Load reference summary
		Text referenceSummary = Utils.loadText("projects/temario-2014/summaries/"
				+ "reference/automatic/", "ce94ab10-a_areference1.txt");
	
		referenceSummary = getSummaryPreProcessingPipeline().process(referenceSummary);

		// Optimization
		String[] varNames = { "k1", "k2", "loc_len", "informatividade" };
		Optimization optmization = new Optimization("flc/fb2015.flc", varNames, text, referenceSummary);

	}
		
}
