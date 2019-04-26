package summ.fuzzy.optimization.functions;

public interface Function {

	public boolean isFeasibleValue(int index, double value);
	public boolean isFeasibleVariable();
	public FunctionDetails getFunctionInfo();
	
}
