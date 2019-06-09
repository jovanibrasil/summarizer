package summ.fuzzy.optimization.mutation;

import summ.fuzzy.optimization.functions.Function;
import summ.fuzzy.optimization.mutation.MutationOperatorFactory.MutationOperatorType;

public class CreepMutation extends MutationOperator {
	
	private MutationOperatorType type;
	private double max;
	private double min; 
	private Function function;
	
	public CreepMutation(MutationOperatorType creepType, Function function) {
		super();
		this.type = creepType;
		this.function = function;
		
		if(this.type.equals(MutationOperatorType.NORMAL_CREEP)) {
			this.max = +0.05; this.min = -0.05;
		}else {
			this.max = 1.2; this.min = 0.98; 
		}
		
	}

	/**
	 * Adiciona um pequeno numero aleatorio ao gene. Este numero aleatorio pode ser obtido atraves
	 * de uma distribuicao normal (media zero e desvio padrao pequeno) ou uma distribuicao uniforme.
	 * 
	 */
	public double creepNormal(double val, double rangeMin, double rangeMax) {
		double result, disturb;
		int counter = 0;
		while(counter < 100) {
			disturb = this.min + (this.max - this.min) * this.rand.nextDouble(); 
			result = val + disturb;
			if(result >= rangeMin && result <= rangeMax) return result;
			counter++;
		}
		return val;
	}
	
	/**
	 * Tambem e possivel obter a mutacao creep atraves da multiplicacao do valor do gene com um valor
	 * muito proximo de um, para apenas gerar uma pequena perturbacao no cromossomo e leva-lo mais 
	 * rapidamente ao topo.
	 * 
	 **/
	public double creepDisturb(double val, double rangeMin, double rangeMax) {
		double result, disturb;
		int counter = 0;
		while(counter < 100) {
			disturb = this.min + (this.max - this.min) * this.rand.nextDouble(); 
			result = val * disturb;
			if(result >= rangeMin && result <= rangeMax) return result;
			counter++;
		}
		return val;
	}
	
	
	
	@Override
	public String toString() {
		return "Creep mutation";
	}
	

	@Override
	public double getAleatoryFeasibleCoefficient(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAleatoryFeasibleCoefficient(double min, double max) {
		if(this.type.equals(MutationOperatorType.NORMAL_CREEP)) {
			return this.creepNormal(this.value, min, max);
		}else {
			return this.creepDisturb(this.value, min, max);
		}
	}	
	
}
