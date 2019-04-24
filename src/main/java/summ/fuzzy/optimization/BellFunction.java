package summ.fuzzy.optimization;


public class BellFunction implements Function {

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
	
	
}
