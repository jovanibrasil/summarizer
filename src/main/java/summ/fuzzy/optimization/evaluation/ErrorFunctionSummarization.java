package summ.fuzzy.optimization.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sourceforge.jFuzzyLogic.optimization.ErrorFunction;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import summ.fuzzy.FuzzySystem;
import summ.model.Text;
import summ.nlp.evaluation.Evaluation;
import summ.nlp.evaluation.EvaluationResult;
import summ.summarizer.Summarizer;
import summ.utils.Tuple;

public class ErrorFunctionSummarization extends ErrorFunction {
	
	private static final Logger log = LogManager.getLogger(ErrorFunctionSummarization.class);
	
	private FuzzySystem fs;
	private Text originalText;
	private Text referenceSummary;
	private Evaluation evaluation;
	private ArrayList<Tuple<Integer>> lastSentenceInformativityValues;
	private List<String> varNames;
	
	public ErrorFunctionSummarization(FuzzySystem fs, Text originalText, Text referenceSummary, Evaluation evaluation, 
			List<String> varNames) {
		super();
		this.fs = fs;
		this.referenceSummary = referenceSummary;
		this.originalText = originalText;
		this.evaluation = evaluation;
		this.varNames = varNames;
	}
	
	public FuzzySystem getFuzzySystem() {
		return this.fs;
	}
	
    public double evaluate(RuleBlock ruleBlock) {
        
        // Generate new summary using the new configuration
     	ArrayList<Tuple<Integer>> sentencesInformativity = Summarizer.computeSentencesInformativity(originalText, this.fs, varNames);
     	
//     	if(lastSentenceInformativityValues != null) {
//     		for (int i = 0; i < sentencesInformativity.size(); i++) {
//     			System.out.println(sentencesInformativity.get(i).y + " - " + lastSentenceInformativityValues.get(i).y);
//			}
//     	}
//     	lastSentenceInformativityValues = sentencesInformativity;
     	
        Text generatedSummary = Summarizer.generateSummary(originalText, referenceSummary.getTotalSentence(), sentencesInformativity);
        // Evaluate the result and calculate the error
        EvaluationResult result = evaluation.evaluate(generatedSummary, referenceSummary);
        
        double error = result.getMetric("fMeasure");

        

//      log.info(result);
      
        
        
        //        System.out.println("Iteratation: " + this.counter + " Error value: " + error + 
//        		" Overlap: " + result + " Reference summary size: " + referenceSummary.getTotalSentence());
//        System.out.println(error);
//        System.out.println(fs);
        
        return error ;
    }
    
} 