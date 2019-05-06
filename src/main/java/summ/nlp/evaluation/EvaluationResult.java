package summ.nlp.evaluation;

import java.util.HashMap;

import summ.nlp.evaluation.EvaluationMethodFactory.EvaluationMethodType;

public class EvaluationResult {

	private HashMap<String, Double> metrics;
	private String evalName;
	private EvaluationMethodType evaluationMetricType;
	
	public EvaluationResult() {
		this.evalName = "";
		this.metrics = new HashMap<>();
	}
	
	public void addMetric(String metricName, Double value) {
		this.metrics.put(metricName, value);
	}
	
	public HashMap<String, Double> getMetrics(){
		return metrics;
	}
	
	public Double getEvaluationMetricValue(){
		return metrics.get(evaluationMetricType.name());
	}
	
	public Double getEvaluationMetricValue(String metricName){
		return metrics.get(metricName);
	}
	
	public String getEvalName() {
		return evalName;
	}

	public void setEvalName(String evalName) {
		this.evalName = evalName;
	}

	public EvaluationMethodType getMainEvaluationMetric() {
		return evaluationMetricType;
	}

	public void setMainEvaluationMetric(EvaluationMethodType mainEvaluationMetric) {
		this.evaluationMetricType = mainEvaluationMetric;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Evalution Result - File name: " + this.evalName + "\n");
		metrics.entrySet().forEach(entry -> {
			sb.append("\t" + entry.getKey() + ": " + entry.getValue() + "\n");
		});
		return sb.toString();
	}
	
}
