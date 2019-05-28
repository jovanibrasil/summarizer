package summ.utils;

public class Tuple<X> implements Comparable<Tuple<X>> {
	
	public final X x;
	public final Double y;
	
	public Tuple(X x, Double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Tuple<X> o) {
		if((double)o.y  < (double)this.y) {
			return -1;
		}else if((double)o.y > (double)this.y) {
			return 1;
		}else {
			return 0;
		}
	}
	
}
