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

import net.sourceforge.jFuzzyLogic.rule.Variable;
import summ.fuzzy.FuzzySystem;
import summ.fuzzy.optimization.Optimization;
import summ.model.Paragraph;
import summ.model.Sentence;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationResult;
import summ.nlp.evaluation.SentenceOverlap;
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
import summ.utils.ExportHTML;
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

	public static ArrayList<Tuple<Integer>> computeSentencesInformativity(Text text, FuzzySystem fs) {
		ArrayList<Tuple<Integer>> outList = new ArrayList<Tuple<Integer>>();
		
		text.getParagraphs().forEach(p -> {
			p.getSentences().forEach(s -> {
				fs.setOutputVariable("informatividade");
				fs.setInputVariable("k1", (Double) s.getFeature("tf-isf"));
				fs.setInputVariable("loc_len", (Double) s.getFeature("loc-len"));
				fs.setInputVariable("k2", (Double) s.getFeature("title-words-relative"));
				double score = fs.evaluate().getValue(); 
				s.setScore(score);
				outList.add(new Tuple<>(s.getId(), score));
			});
		});
		Collections.sort(outList);
		return outList;
	}

	public static Text generateSummary(Text text, int summarySize, ArrayList<Tuple<Integer>> outList) {
		
		// Generate the summary
		Text generatedSummary = new Text("");
		Paragraph paragraph = new Paragraph(""); // TODO where is the full paragraph text? Is it necessary?
		int count = 0;
		for (Tuple<Integer> t : outList) {
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

	public static Text summarize(Text text, int summarySize, String systemPath) {

		// Pre-processing
		text = getTextPreProcessingPipeline().process(text);
		text = featureComputation(text);
		
		// Summary generation
		FuzzySystem fs = new FuzzySystem(systemPath);
		
		// Compute sentences informativity using fuzzy system
		ArrayList<Tuple<Integer>> sentencesInformativity = computeSentencesInformativity(text, fs);
		
		// int summarySize = (int)(0.3 * text.getTotalSentence());
		Text generatedSummary = generateSummary(text, summarySize, sentencesInformativity);
		
//		System.out.println(generatedSummary);
//		
//		System.out.println("-----------------------------------------");
//		System.out.println(text);
		
		return generatedSummary;
	}
	
	public static void summarizationText(String fileName, String systemPath) {
		// Load complete text
		Text text = Utils.loadText("projects/temario-2014/full-texts/", fileName);
		Text referenceSummary = Utils.loadText("projects/temario-2014/summaries/"
					+ "reference/automatic/", fileName.replace(".txt", "") + "_areference1.txt");
		
		referenceSummary = getSummaryPreProcessingPipeline().process(referenceSummary);

		int summarySize = referenceSummary.getTotalSentence();
		Text generatedSummary = summarize(text, summarySize, systemPath);
		
		Evaluation so = new SentenceOverlap();
		EvaluationResult result = so.evaluate(generatedSummary, referenceSummary);	
		System.out.println(result);
		
		ExportCSV.exportSentenceFeatures(text);
		ExportHTML.exportSentecesAndFeatures(text, Arrays.asList("relative-len", "relative-location", "tf-isf", "title-words-relative"));
		ExportHTML.exportHighlightText(text, generatedSummary.getSentencesMap());
		ExportHTML.exportOverlappedFeatures(generatedSummary, referenceSummary);
		
	}

	public static void summarizeTexts(String systemPath) {
		
		HashMap<String, Text> texts = Utils.loadTexts("projects/temario-2014/full-texts/", null);
		HashMap<String, Text> refSummaries = Utils.loadTexts("projects/temario-2014/summaries/"
				+ "reference/automatic/", null);
		
		try {
			
			File file = new File("results/texts-evaluation-"+(new Date()).toString());  
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);

	        String[] header = new String[] { "textName", "precision", "recall", "fMeasure", "retrievedSentences", "relevantSentences", "correctSentences" };
	        writer.writeNext(header);
	        
			for (Entry<String, Text> entry : texts.entrySet()) {
				
				//System.out.println("Processing " + entry.getValue().getName() + " ...");
				
				Text referenceSummary = refSummaries.get(entry.getKey());
				Text originalText = entry.getValue();
				
				referenceSummary = getSummaryPreProcessingPipeline().process(referenceSummary);
				
				// Summarize
				int summarySize = referenceSummary.getTotalSentence();
				Text generatedSummary = summarize(originalText, summarySize, systemPath);
				
				// Evaluate generated summary
				
				Evaluation so = new SentenceOverlap();
				EvaluationResult result = so.evaluate(generatedSummary, referenceSummary);	
				result.setEvalName(entry.getValue().getName());
				
				System.out.println(result);
				
				// Save summarization result
				String[] data = { entry.getValue().getName(), result.getMetric("precision").toString(), result.getMetric("recall").toString(), 
					result.getMetric("fMeasure").toString(), result.getMetric("retrievedSentences").toString(), result.getMetric("relevantSentences").toString(),
						result.getMetric("correctSentences").toString() };
				
				//writer.writeNext(data);
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
		Evaluation so = new SentenceOverlap();
		Optimization optmization = new Optimization("fcl/fb2015.fcl", varNames, text, referenceSummary, so);

	}
		
}
