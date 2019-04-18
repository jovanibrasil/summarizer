package summ.fuzzy.optimization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class CustomLinguisticTerm implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 2447337688400816782L;
	
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

	public CustomLinguisticTerm deepClone() {
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (CustomLinguisticTerm) ois.readObject();
			
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return parameters.toString();
	}
	
}

