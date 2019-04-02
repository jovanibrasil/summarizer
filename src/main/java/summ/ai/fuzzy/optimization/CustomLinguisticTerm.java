package summ.ai.fuzzy.optimization;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class CustomLinguisticTerm {
	public String termName; 
	public RealVector parameters;
	
	public CustomLinguisticTerm() {
		this.parameters = new ArrayRealVector();
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public RealVector getParameters() {
		return parameters;
	}

	public void setParameters(RealVector parameters) {
		this.parameters = parameters;
	}
	
	public void appedParameter(double parameter) {
		this.parameters.append(parameter);
	}
	
	public void appendParameter(RealVector parameters) {
		this.parameters.append(parameters);
	}

	
}

