package summ.fuzzy.optimization.functions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FunctionDetails implements Cloneable, Serializable {
	
	private static final long serialVersionUID = -7215582134407137173L;
	double rangeMin[];
	double rangeMax[];
	
	public FunctionDetails(double rangeMin[], double rangeMax[]) {
		this.rangeMin = rangeMin; 
		this.rangeMax = rangeMax;
	}

	public double getRangeMin(int index) {
		return rangeMin[index];
	}

	public double getRangeMax(int index) {
		return rangeMax[index];
	}
	
	public Function deepClone() {
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Function) ois.readObject();
			
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
}