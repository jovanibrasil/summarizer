package summ.fuzzy.optimization.functions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BellFunction implements Function, Cloneable, Serializable {

	private static final long serialVersionUID = -4803960395394891315L;

	private FunctionDetails functionInfo;
	
	public BellFunction() {
		functionInfo = new FunctionDetails(new double[] { 0.1, 0.1, 0.1}, 
				new double[] {1.0, 5.0, 1.0} );	
	}
	
	@Override
	public boolean isFeasibleValue(int index, double value) {
		if(index == 0) {
			if(value > 0 && value <= 1) {
				return true;
			}		
		}else if(index == 1) {
			if(value > 0 && value <= 20) {
				return true;
			}		
		}else if(index == 2) {
			if(value >= 0 && value <= 1) {
				return true;
			}
		}
		return false;
	}
	

	@Override
	public boolean isFeasibleVariable() {
		return false;
	}

	@Override
	public FunctionDetails getFunctionInfo() {
		return functionInfo;
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
