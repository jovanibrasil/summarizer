package summ.utils;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class MathUtils {

	/**
	 * Calculate the square root for each entry by the argument. Returns a new vector. 
	 * Does not change instance data.
	 * 
	 */
	public static RealVector ebeSqrt(RealVector vector) {
		RealVector result = new ArrayRealVector(vector.getDimension());
		for (int i = 0; i < vector.getDimension(); i++) {
			result.addToEntry(i, Math.sqrt(vector.getEntry(i)));
		}
		return result;
	}
	
}
