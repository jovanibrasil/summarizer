package summ.fuzzy.optimization;

public class FunctionDetails {
	
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
	
}