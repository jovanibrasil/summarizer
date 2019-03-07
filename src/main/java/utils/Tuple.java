package utils;

public class Tuple<X, Y> implements Comparable<Tuple<X, Y>> {
	
	public final X x;
	public final Y y;
	
	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Tuple<X, Y> o) {
		if((double)o.y < (double)this.y) {
			return -1;
		}else if((double)o.y > (double)this.y) {
			return 1;
		}else {
			return 0;
		}
	}
	
}
