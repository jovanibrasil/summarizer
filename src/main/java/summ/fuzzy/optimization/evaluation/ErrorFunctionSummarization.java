package summ.fuzzy.optimization.evaluation;

import java.util.List;

import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.FuzzySystem;
import summ.model.Text;
import summ.nlp.evaluation.EvaluationMethod;
import summ.summarizer.Summarizer;

public class ErrorFunctionSummarization implements ErrorFunction {
	
	private static final Logger log = LogManager.getLogger(ErrorFunctionSummarization.class);
	
	private List<String> varNames;
	private List<Text> textList;
	private Summarizer summarizer;	
	
	public ErrorFunctionSummarization(Summarizer summarizer, List<Text> texts,
			EvaluationMethod evaluationMethod, List<String> varNames) {
		super();
		this.textList = texts;
		this.varNames = varNames;
		this.summarizer = summarizer;
	}
	
	/**
	 * Get the minimum value between two values v1 and v2. 
	 * 
	 * @param v1 is the first value.
	 * @param v2 is the second value.
	 * @return the minimum value between v1 and v2.
	 */
	public double min(double v1, double v2) {
		return v1 < v2 ? v1 : v2;
	}
	
	/**
	 * Get the maximum value between two values v1 and v2. 
	 * 
	 * @param v1 is the first value.
	 * @param v2 is the second value.
	 * @return the maximum value between v1 and v2.
	 */
	public double max(double v1, double v2) {
		return v1 > v2 ? v1 : v2;
	}

	/**
	 * Calculates the overlap between two adjacent memberships.
	 * 
	 * @param c1 center of the left membership.
	 * @param w1 right span of the left membership.
	 * @param c2 center of the right membership.
	 * @param w2 left span of the right membership.
	 * @return the overlap between the functions.
	 */
	public double overlap(double c1, double  w1, double c2, double w2) {
		double r = (c1 + w1) - (c2 -w2);
		return r >= 0 ? r : 0;
	}
	
	/**
	 * Calculates the functions coverage range of an variable.
	 * 
	 * @param chromossome is the real codified chromossome.
	 * @param varIdx is the variable index inside of the chromossome.
	 * @return 	
	 */
	public double range(RealVector chromossome, int varIdx) {
		double total_range = 0;
		for (int index = varIdx; index < varIdx + 9; index+=3) { // iterate functions
			total_range += chromossome.getEntry(index) * 2; // sum function width
		}
		return total_range;
	}
	
	/**
	 * Calculates the functions coverage factor. 
	 * 
	 * @param chromossome is the the real codified chromossome.
	 * @param varIdx is the variable index inside of the chromossome.
	 * @return the coverage factor.
	 */
	public double coverage_factor(RealVector chromossome, int varIdx) {
		double region = 1.0; // universe of discourse for a fuzzy variable
		double total_range = range(chromossome, varIdx);
		return 1.0 / total_range / region;
	}

	/**
	 * Calculate the functions overlap ratio.
	 * 
	 * @param chromossome is the the real codified chromossome.
	 * @param varIdx is the variable index inside of the chromossome.
	 * @return the overlap ratio.
	 */
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
	
	/**
	 * Evaluates the membership functions of all linguistic variables codified in the chromossome.
	 * 
	 * @param chromossome is the the real codified chromossome.
	 * @return the fuzzy system evaluation
	 */
	public double functionsEvaluation(RealVector chromossome) {
		double f = 0.0;
		for (int varIndex = 0; varIndex < chromossome.getDimension(); varIndex+=9) { // iterate variables
			f += overlap_factor(chromossome, varIndex) + coverage_factor(chromossome, varIndex);
		}
		return 1.0/f;
	}
	
	/**
	 * Evaluates the fuzzy system. This function summarize and evaluate set of documents using the 
	 * specified fuzzy system. The result is a average value between all evaluations.
	 * 
	 * @param fuzzySystem 
	 * @return the fuzzy system evaluation
	 * 
	 */
    public double summariesEvaluation(FuzzySystem fuzzySystem) {
        double acc = 0.0;
        long startTime = System.nanoTime();
        for (Text text : textList) {
        	Text generatedSummary = this.summarizer.summarizeText(text, fuzzySystem, varNames, false);       
            acc += generatedSummary.getEvaluationResult().getEvaluationMetricValue(); 
		}
        long timeElapsed = (System.nanoTime() - startTime) / 1000000;
		log.debug("Chromosome evaluation time (s) : " + timeElapsed / 1000 + " Evaluation result: " + acc / this.textList.size());
		return acc / this.textList.size();
    }
    
} 