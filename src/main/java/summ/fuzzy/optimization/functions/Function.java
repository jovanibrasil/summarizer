package summ.fuzzy.optimization.functions;


public abstract class Function {

	double rangeMin[];
	double rangeMax[];
	
	public Function(double rangeMin[], double rangeMax[]) {
		this.rangeMin = rangeMin; 
		this.rangeMax = rangeMax;
	}
	
	public double getRangeMin(int index) {
		return rangeMin[index];
	}

	public double getRangeMax(int index) {
		return rangeMax[index];
	}
	
	public abstract boolean isFeasibleValue(int index, double value);
	public abstract boolean isFeasibleVariable();
	
}
