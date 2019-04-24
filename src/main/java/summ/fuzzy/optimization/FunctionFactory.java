package summ.fuzzy.optimization;


public class FunctionFactory {
	
	public static Function generateFunctionInfo(FunctionType functionType) {	
		switch (functionType) {
			case BELL:
				return new BellFunction();
			default:
				break;
		}
		return null;
	}
	
}
