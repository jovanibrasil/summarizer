package summ.ai.fuzzy.optimization;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/*
 * This class encapsulates an candidate solution and the fitness value. 
 */
public class Chromosome implements Comparable<Chromosome> {
	
	double fitness;
	List<CustomVariable> variables;
	
	public Chromosome() {
		this.variables = new ArrayList<>();
		this.fitness = 0;
	}
	
	public void addGene(CustomVariable variable) {
		this.variables.add(variable);
	}
	
	public void appendGenes(List<CustomVariable> variables) {
		this.variables.addAll(variables);
	}
	
	public List<CustomVariable> getVariables() {
		return this.variables;
	}
	
	@Override
	public int compareTo(Chromosome o) {
		if(o.fitness < (double)this.fitness) {
			return -1;
		}else if(o.fitness > this.fitness) {
			return 1;
		}else {
			return 0;
		}
	}
	
}
