package summ.fuzzy.optimization.mutation;

import java.util.HashSet;
import java.util.Random;

import summ.fuzzy.optimization.Chromosome;

public abstract class MutationOperator {
	
	//private static final Logger log = LogManager.getLogger(MutationOperator.class);
	
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
	
	public Chromosome mutateGenes(Chromosome chromosome, double genesMutationPercentual) {
		
		HashSet<Integer> alreadyMutated = new HashSet<Integer>();
		alreadyMutated.clear();
		
		int maxGeneMutatedValues = (int)(chromosome.getLength() * genesMutationPercentual);
		int geneMutationCounter = 0;
		
		while (geneMutationCounter < maxGeneMutatedValues) {	
			int geneIndex = getAleatoryIntegerValue(0, chromosome.getLength()-1);
			
			if(this.alreadyMutated.contains(geneIndex)) continue;
			
			// descobrir o indice do termo a partir do indice do variável
			// resto da divisão por 3 (isso pq todas as varieis tem o mesmo tamanho)
			
			double newGeneValue = this.getAleatoryFeasibleCoefficient(geneIndex % 3);
			chromosome.setGene(geneIndex, newGeneValue);
			geneMutationCounter++;
			alreadyMutated.add(geneIndex);
			MutationOperator.VALUE_GEN_HIT++;
		}
		return chromosome;
	}
		
}
