package summ.fuzzy.optimization.functions;

import java.io.Serializable;

public class GbellFunction extends Function implements Cloneable, Serializable {

	//private static final Logger log = LogManager.getLogger(BellFunction.class);
	private static final long serialVersionUID = -4803960395394891315L;

	public GbellFunction() {
		super(new double[] { 0.05, 1.0, 0.0}, 
				new double[] {0.3, 5.0, 1.0});	
	}
	
	@Override
	public boolean isFeasibleValue(int index, double value) {
		if(index == 0) {
			if(value > getRangeMin(0) && value <= getRangeMax(0)) {
				return true;
			}		
		}else if(index == 1) {
			if(value > getRangeMin(1) && value <= getRangeMax(1)) {
				return true;
			}		
		}else if(index == 2) {
			if(value >= getRangeMin(2) && value <= getRangeMax(2)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFeasibleVariable() {
		return false;
	}
	
}
