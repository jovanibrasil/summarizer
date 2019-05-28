package summ.fuzzy.optimization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/*
 * This class encapsulates an candidate solution and the fitness value. 
 */
public class Chromosome implements Comparable<Chromosome>, Cloneable, Serializable {
	
	private static final long serialVersionUID = -4779057552656984319L;
	
	double fitness;
	private RealVector genes;
	
	public Chromosome() {
		this.genes = new ArrayRealVector(36);
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

	public RealVector getGenes() {
		return this.genes;
	}

	public double getGene(int index) {
		return this.genes.getEntry(index);
	}
	
	public void setGenes(RealVector coefficients) {
		this.genes = coefficients;
	}

	public int getLength() {
		return this.genes.getDimension();
	}

	public void setGene(int geneIndex, double newGeneValue) {
		this.genes.setEntry(geneIndex, newGeneValue);
	}
	
	@Override
	public String toString() {
		return genes.toString();
	}
	
}
