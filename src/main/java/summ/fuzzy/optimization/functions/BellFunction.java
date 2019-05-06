package summ.fuzzy.optimization.functions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import summ.fuzzy.optimization.CustomLinguisticTerm;
import summ.fuzzy.optimization.CustomVariable;

public class BellFunction implements Function, Cloneable, Serializable {

	//private static final Logger log = LogManager.getLogger(BellFunction.class);
	private static final long serialVersionUID = -4803960395394891315L;

	private FunctionDetails functionInfo;
	
	public BellFunction() {
		functionInfo = new FunctionDetails(new double[] { 0.2, 1.5, 0.3}, 
				new double[] {0.5, 3.0, 1.0} );	
	}
	
	private boolean isFeasibleValue(int index, double value) {
		if(index == 0) {
			if(value > functionInfo.getRangeMin(0) && value <= functionInfo.getRangeMax(0)) {
				return true;
			}		
		}else if(index == 1) {
			if(value > functionInfo.getRangeMin(1) && value <= functionInfo.getRangeMax(1)) {
				return true;
			}		
		}else if(index == 2) {
			if(value >= functionInfo.getRangeMin(2) && value <= functionInfo.getRangeMax(2)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isFeasibleValue(int index, String termName, double value, CustomVariable variable) {

		// testa se valores estão entre os limites admitidos pela função
		if(!isFeasibleValue(index, value)) return false;
		
		// Testa se o valor da parâmetro c é válido através da comparação com o centro dos outros termos
		if(index == 2) {
			CustomLinguisticTerm term = variable.getLinguisticTerm(termName);
			double a = term.getParameter(0);
			double max, min;
			if(term.getTermName().equals("baixo")) {
				max = variable.getLinguisticTerm("medio").getParameter(2);
				if(value + a < max) return true; // menor que o primeiro
				return false;
			} if(term.getTermName().equals("medio")) {
				min = variable.getLinguisticTerm("baixo").getParameter(2);
				max = variable.getLinguisticTerm("alto").getParameter(2);
				if(value - a > min && value + a < max) return true;
				return false;
			} else {
				max = variable.getLinguisticTerm("medio").getParameter(2);
				if(value - a > max) return true; // maior que o ultimo
				return false;
			}	
		}
		return true;
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
