package summ.fuzzy.optimization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * This class encapsulates an candidate solution and the fitness value. 
 */
public class Chromosome implements Comparable<Chromosome>, Cloneable, Serializable {
	
	private static final long serialVersionUID = -4779057552656984319L;
	
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
	
	public Chromosome deepClone() {
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Chromosome) ois.readObject();
			
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "Chromosome [fitness=" + fitness + ", variables=" + variables + "]";
	}
	
}
