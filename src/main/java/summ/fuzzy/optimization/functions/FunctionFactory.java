package summ.fuzzy.optimization.functions;


public class FunctionFactory {
	
	public static Function generateFunctionInfo(FunctionType functionType) {	
		switch (functionType) {
			case GBELL:
				return new GbellFunction();
			default:
				break;
		}
		return null;
	}
	
}
