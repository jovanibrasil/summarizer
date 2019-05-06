package summ.fuzzy.optimization.functions;

import summ.fuzzy.optimization.CustomVariable;

public interface Function {

	public boolean isFeasibleValue(int index, String termName, double value, CustomVariable variable);
	public boolean isFeasibleVariable();
	public FunctionDetails getFunctionInfo();
	
}
