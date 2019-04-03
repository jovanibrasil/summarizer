package summ.ai.fuzzy.optimization;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class CustomLinguisticTerm {
	
	private String termName; 
	private RealVector parameters;
	
	public CustomLinguisticTerm(Integer termSize, String name) {
		this.parameters = new ArrayRealVector(termSize);
		this.termName = name;
	}

	public String getTermName() {
		return termName;
	}
	
	public RealVector getParameters() {
		return parameters;
	}

	public void setParameters(RealVector parameters) {
		this.parameters = parameters;
	}
	
	public void setParameter(int index, double value) {
		this.parameters.setEntry(index, value);
	}
	
	public double getParameter(int index) {
		return this.parameters.getEntry(index);
	}
	
	public int getParametersLength() {
		return this.parameters.getDimension();
	}
	
}

