package summ.fuzzy.optimization.mutation;

import java.util.HashSet;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.optimization.Chromosome;

public abstract class MutationOperator {
	
	private static final Logger log = LogManager.getLogger(MutationOperator.class);
	
	public static int VALUE_GEN_HIT = 0;
	public static int VALUE_GEN_ERROR = 0;
	public final double GENE_MUTATION_PROBABILITY = 0.3;
	HashSet<Integer> alreadyMutated;
	
	public Random rand;
	
	public MutationOperator() {
		this.rand = new Random();
		this.alreadyMutated = new HashSet<Integer>();
	}
	
	public int getAleatoryIntegerValue(int rangeMin, int rangeMax) {
		return this.rand.nextInt(rangeMax) + rangeMin; // uniformly distributed double
	}
	
	public abstract double getAleatoryFeasibleCoefficient(int index);
	public abstract double getAleatoryFeasibleCoefficient(double min, double max);
	
	public Chromosome mutateGenes(Chromosome chromosome, double genesMutationPercentual) {
		
		HashSet<Integer> alreadyMutated = new HashSet<Integer>();
		alreadyMutated.clear();
		
		int maxGeneMutatedValues = (int)(chromosome.getLength() * genesMutationPercentual);
		int geneMutationCounter = 0;
		
		while (geneMutationCounter < maxGeneMutatedValues) {	
			int geneChromosomeIndex = getAleatoryIntegerValue(0, chromosome.getLength()-1);
			
			if(this.alreadyMutated.contains(geneChromosomeIndex)) continue;
			
			int geneIndex = geneChromosomeIndex % 3;
			
			double newGeneValue = this.getAleatoryFeasibleCoefficient(geneIndex);
			
//			while(true) {
//				if(geneIndex != 2) break;
//				if(geneChromosomeIndex == 1 || geneChromosomeIndex == 10 || 
//						geneChromosomeIndex == 19 || geneChromosomeIndex == 28) break;
//				if(newGeneValue > chromosome.getGene(geneChromosomeIndex)-3) break;
//				newGeneValue = this.getAleatoryFeasibleCoefficient(geneIndex);
//			}
			
			log.debug("Gene mutation - Index: " + geneIndex + " value: " + newGeneValue);
			chromosome.setGene(geneChromosomeIndex, newGeneValue);
			geneMutationCounter++;
			alreadyMutated.add(geneChromosomeIndex);
			MutationOperator.VALUE_GEN_HIT++;
		}
		return chromosome;
	}
		
}
