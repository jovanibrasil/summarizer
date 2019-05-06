package summ.fuzzy.optimization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomVariable implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 5242738091846141697L;
	
	private List<CustomLinguisticTerm> linguisticTerms;
	private String name;
	
	public CustomVariable(String name) {
		this.linguisticTerms = new ArrayList<>();
		this.name = name;
	}
	
	public void addLinguisticTerm(CustomLinguisticTerm linguisticTerm) {
		this.linguisticTerms.add(linguisticTerm);
	}
	
	public CustomLinguisticTerm getLinguisticTerm(String termName) {
		for (CustomLinguisticTerm customLinguisticTerm : linguisticTerms) {
			if(customLinguisticTerm.getTermName().equals(termName)) {
				return customLinguisticTerm;
			}
		}
		return null;
	}
	
	public List<CustomLinguisticTerm> getLinguisticTerms(){
		return this.linguisticTerms;
	}
	public String getName() {
		return this.name;
	}
	
	public CustomVariable deepClone() {
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (CustomVariable) ois.readObject();
			
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return linguisticTerms.toString();
	}

	public double[] getCenters() {
		double[] centers = new double[3];
		int i = 0;
		for (CustomLinguisticTerm customLinguisticTerm : linguisticTerms) {
			centers[i++] = customLinguisticTerm.getParameter(2); // center 
		}
		return centers;
	}
		
}
