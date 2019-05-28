package summ.fuzzy.optimization.evaluation;

import java.util.List;

import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.FuzzySystem;
import summ.model.Text;
import summ.nlp.evaluation.EvaluationMethod;
import summ.nlp.evaluation.EvaluationResult;
import summ.summarizer.Summarizer;

public class ErrorFunctionSummarization implements ErrorFunction {
	
	private static final Logger log = LogManager.getLogger(ErrorFunctionSummarization.class);
	
	private List<String> varNames;
	private List<Text> textList;
	private Summarizer summarizer;	
	private EvaluationMethod evaluationMethod;
	
	public ErrorFunctionSummarization(Summarizer summarizer, List<Text> texts,
			EvaluationMethod evaluationMethod, List<String> varNames) {
		super();
		this.evaluationMethod = evaluationMethod;
		this.textList = texts;
		this.varNames = varNames;
		this.summarizer = summarizer;
	}
	
	public double min(double v1, double v2) {
		return v1 < v2 ? v1 : v2;
	}
	
	public double max(double v1, double v2) {
		return v1 > v2 ? v1 : v2;
	}

	/**
	 * Overlap ration of the membership.
	 * 
	 * @param c1 centro um
	 * @param w1 lado um
	 * @param c2 centro dois
	 * @param w2 lado dois
	 * @return
	 */
	public double overlap(double c1, double  w1, double c2, double w2) {
		double r = (c1 + w1) - (c2 -w2);
		return r >= 0 ? r : 0;
	}
	
	// coverage range of the functions
	public double range(RealVector chromossome, int varInitIdx) {
		double total_range = 0;
		for (int index = varInitIdx; index < varInitIdx + 9; index+=3) { // iterate functions
			total_range += chromossome.getEntry(index) * 2; // sum function width
		}
		return total_range;
	}
	
	public double coverage_factor(RealVector chromossome, int varIdx) {
		double region = 1.0; // universe of discourse for a fuzzy variable
		double total_range = range(chromossome, varIdx);
		return 1.0 / total_range / region;
	}

	// overlap ratio 
	public double overlap_factor(RealVector chromossome, int varIdx) {
		double overlap_factor = 0;
		double w1 = chromossome.getEntry(varIdx);
		double c1 = chromossome.getEntry(varIdx+2);
		double w2 = chromossome.getEntry(varIdx+3);
		double c2 = chromossome.getEntry(varIdx+5);
		overlap_factor += max(overlap(c1, w1, c2, w2) / min(w1, w2), 1.0) - 1; // compare low with medium
		varIdx += 9;
		if(varIdx < chromossome.getDimension()) {
			c1 = chromossome.getEntry(varIdx+2);
			chromossome.getEntry(varIdx+5);
			chromossome.getEntry(varIdx);
			chromossome.getEntry(varIdx+3);
			overlap_factor += max(overlap(c1, w1, c2, w2) / min(w1, w2), 1.0) - 1; // compare medium with high
		}
		return overlap_factor;
	}
	
	public double f(RealVector chromossome) {
		double f = 0.0;
		for (int varIndex = 0; varIndex < chromossome.getDimension(); varIndex+=9) { // iterate variables
			f += overlap_factor(chromossome, varIndex) + coverage_factor(chromossome, varIndex);
		}
		return 1.0/f;
	}
	
	/**
	 *  Evaluate the result and calculate the error.     
	 * 
	 */
    public double evaluate(FuzzySystem fuzzySystem) {
        double acc = 0.0;
        long startTime = System.nanoTime();
        for (Text text : textList) {
        	Text generatedSummary = this.summarizer.summarizeText(text, fuzzySystem, varNames);
        	EvaluationResult result = this.summarizer.evaluateSummary(evaluationMethod, generatedSummary, text.getReferenceSummary());
        	log.debug(text.getName() + " - " + result.getEvaluationMetricValue());
            log.trace(result);
            acc += result.getEvaluationMetricValue() + f(fuzzySystem.getCoefficients());
		}
        long timeElapsed = (System.nanoTime() - startTime) / 1000000;
		log.debug("Chromosome evaluation time (s) : " + timeElapsed / 1000 + " Evaluation result: " + acc / this.textList.size());
		return acc / this.textList.size();
    }
    
} 