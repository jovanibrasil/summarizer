package summ.fuzzy.optimization;

import java.util.Random;

public class BellFunction {

	public static boolean isFeasibleValue(int index, double value) {
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
	
	public static double getAleatoryFeasibleValue(int index) {
		Random rand = new Random();
		double rangeMin = 0;
		double rangeMax = 0;
		if(index == 0) {
			rangeMin = 0.1; 
			rangeMax = 1;
		}else if(index == 1) {
			rangeMin = 0.1;
			rangeMax = 20;
		}else if(index == 2) {
			rangeMin = 0.1;
			rangeMax = 1;
		}
		return rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
	}
	
	public static boolean isFeasibleVariable() {
		// TODO
		return false;
	}
	
	
}
