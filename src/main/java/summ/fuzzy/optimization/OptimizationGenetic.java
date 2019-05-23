package summ.fuzzy.optimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import summ.fuzzy.FuzzySystem;
import summ.fuzzy.optimization.crossover.CrossoverOperator;
import summ.fuzzy.optimization.evaluation.ErrorFunctionSummarization;
import summ.fuzzy.optimization.functions.BellFunction;
import summ.fuzzy.optimization.mutation.MutationOperator;
import summ.fuzzy.optimization.mutation.UniformMutation;

public class OptimizationGenetic {
	
	private static final Logger log = LogManager.getLogger(OptimizationGenetic.class);
	
	public int populationSize;	
	public List<Chromosome> currentPopulation;
	public double crossoverProbability; // probability of crossover operation
    public double chromossomeMutationProbability; // probability of mutation operation
    public boolean elitism; 
    
    public CrossoverOperator crossoverOperator;
	public MutationOperator mutationOperator;
    public ErrorFunctionSummarization errorFunction;
    public Random rand;
	
    public int iteration;
    boolean[] randControl;
    public double geneMutationPercentual;
    List<String> parameters;
    	
    public List<Double> bestFitnessSerie;
    public List<Double> averageFitnessSerie;
    public List<Double> worstFitnessSerie;
    
    public FuzzySystem fuzzySystem;

    public static int CROSSOVER_COUNTER = 0;
	public static int MUTATION_VERIFICATION = 0;
	public static int MUTATION_EXECUTION = 0;
    
	private int maxIterations;
    
    /**
	 * Genetic optimization constructor.
	 * 
	 * @param fuzzyRuleSet is the initial fuzzy rule set
	 * 
	 */
	public OptimizationGenetic(ErrorFunctionSummarization errorFunction,
			CrossoverOperator crossoverOperator, MutationOperator mutationOperator, double crossoverProbability,
			double chromosomeMutationProbability,  double geneMutationProbability, boolean eletism, int populationSize,
			List<String> parameters, FuzzySystem fuzzySystem, int maxIterations) {
		
		this.currentPopulation = new ArrayList<>();
		this.populationSize = populationSize;
		this.crossoverOperator = crossoverOperator;
		this.mutationOperator = mutationOperator;
		this.errorFunction = errorFunction;
		this.crossoverProbability = crossoverProbability;
		this.chromossomeMutationProbability = chromosomeMutationProbability;
        this.geneMutationPercentual = geneMutationProbability;
		this.elitism = eletism;
		this.rand = new Random();
        this.parameters = parameters;
        this.iteration = 0;
        
        this.bestFitnessSerie = new ArrayList<>();
        this.averageFitnessSerie = new ArrayList<>();
        this.worstFitnessSerie = new ArrayList<>();

        this.maxIterations = maxIterations;
        
        this.fuzzySystem = fuzzySystem;
        
        optimizeInit();
	}
	
	/**
	 * Generate the first population, evaluate and rank the initial population.
	 */
	public void optimizeInit() {
		log.debug("Generate the first population, evaluate and rank the initial population.");
		generateFirstPopulation();
		evaluatePopulation();
		rankPopulation();
		log.info("Mutation verification: " + OptimizationGenetic.MUTATION_VERIFICATION);
		log.info("Mutation execution: " + OptimizationGenetic.MUTATION_EXECUTION);
		log.info("Mutation operator hits: " + MutationOperator.VALUE_GEN_HIT);
		log.info("Mutation operator errors: " + MutationOperator.VALUE_GEN_ERROR);
	}

	public void optimizeIteration(int iterationNum) {
		
		this.iteration++;
		log.info("Iterations:	" + this.iteration + "/" + this.maxIterations);
		generateIntermediatePopulation();				
		evaluatePopulation();
		rankPopulation();	
		log.info("Mutation verification: " + OptimizationGenetic.MUTATION_VERIFICATION);
		log.info("Mutation execution: " + OptimizationGenetic.MUTATION_EXECUTION);
		log.info("Mutation operator hits: " + MutationOperator.VALUE_GEN_HIT);
		log.info("Mutation operator errors: " + MutationOperator.VALUE_GEN_ERROR);
		
	}
    
	public void generateFirstPopulation() {
		
		log.debug("Generating initial population...");
		 
		this.currentPopulation = new ArrayList<>(); // candidate solution matrix
		
		// Create the first Solution. The first Solution is a copy of the initial solution.
		Chromosome chromosome = new Chromosome();
		chromosome.setGenes(fuzzySystem.getCoefficients());
		log.info("Seeding: " + chromosome);
		this.currentPopulation.add(chromosome);
		
		MutationOperator fpMutator = new UniformMutation(new BellFunction());
		
		for (int i = 1; i < this.populationSize; i++) {
			chromosome = new Chromosome();
			for (int j = 0; j < fuzzySystem.getCoefficients().getDimension(); j+=3) {
				chromosome.setGene(j, fpMutator.getAleatoryFeasibleCoefficient(0));
				chromosome.setGene(j+1, fpMutator.getAleatoryFeasibleCoefficient(1));
				chromosome.setGene(j+2, fpMutator.getAleatoryFeasibleCoefficient(2));
			}
			this.currentPopulation.add(chromosome);
		}	
		
	}
	
	public int countDuplicates(List<Chromosome> population) {
		log.debug("Verifying duplicates ...");
		int c = 0;
		
		for (int i = 0; i < population.size(); i++) {
			for (int j = 0; j < population.size(); j++) {
				if(i != j && population.get(i).equals(population.get(j))) {
					log.info("Duplicated: " + population.get(i));
					c++; 
				}
			}
		}
		return c;
	}
	
	/**
	 * Sort the population by fitness.
	 */
	public void rankPopulation() {
		
	 	int duplicatedCounter = countDuplicates(this.currentPopulation);
		log.info("Current population has " + duplicatedCounter + " duplicated chromosomes ...");
		
		log.debug("Ranking population " + this.iteration);
		Collections.sort(this.currentPopulation);
		
		for (int index = 0; index < currentPopulation.size(); index++) {
			log.info("[" + index + "]" + currentPopulation.get(index).fitness);
		}
		
		double averageFitness = this.currentPopulation.stream()
				.mapToDouble(x -> x.fitness).sum() / this.currentPopulation.size();
		this.bestFitnessSerie.add(this.getBestIndividual().fitness);
		this.worstFitnessSerie.add(this.getWorstIndividual().fitness);
		this.averageFitnessSerie.add(averageFitness);
		log.info("Best individual: " + this.getBestIndividual());
		log.info("Average fitness: " + averageFitness);
		log.info("Best fitness: " + this.getBestIndividual().fitness);
		log.info("Worst fitness: " + this.getWorstIndividual().fitness);
		log.info("Crossover execution: " + OptimizationGenetic.CROSSOVER_COUNTER);
		OptimizationGenetic.CROSSOVER_COUNTER = 0;
	}
	
	private Chromosome getWorstIndividual() {
		return this.currentPopulation.get(this.currentPopulation.size()-1);
	}

	/**
	 * Return the best individual. The best individual is the individual with the best
	 * fitness in the current generation. 
	 */
	public Chromosome getBestIndividual() {
		return this.currentPopulation.get(0);
	}
	
	/**
	 * Select a random Solution from the population.
	 */
	public Chromosome randomSelection() {
		Random rand = new Random();
		return this.currentPopulation.get(rand.nextInt(this.populationSize));		
	}
	
	/**
	 * Select a random set of Solutions from the population and return the 
	 * fittest one. Randomically select a set of Solutions from the population.
	 */
	public Chromosome tournamentSelection(int tournnamentSize) {
		int bestChromosomeIndex = -1;
				
		int i = 0;
		while (i < tournnamentSize) {
			int randIndex = rand.nextInt(this.populationSize);
			if(!randControl[randIndex]) {
				if (bestChromosomeIndex < 0 || this.currentPopulation.get(randIndex).fitness > 
					this.currentPopulation.get(bestChromosomeIndex).fitness) {
					bestChromosomeIndex = randIndex;
				}
				i++;
			}
		}
		randControl[bestChromosomeIndex] = true;
		return this.currentPopulation.get(bestChromosomeIndex);
	}
	
	public void evaluatePopulation() {
		
		for (Chromosome chromosome : currentPopulation) {
			this.fuzzySystem.setCoefficients(chromosome.getGenes());
			chromosome.fitness = errorFunction.evaluate(this.fuzzySystem);
		}
	}
	
	public void generateIntermediatePopulation() {
		
		log.debug("Generate intermediate population");
		List<Chromosome> population = new ArrayList<>();
		
		if(this.elitism) {
			Chromosome elite = this.getBestIndividual();
			population.add(elite.deepClone());
		}
		
		List<Chromosome> children = null;
		while(population.size() < this.populationSize) {
		
			this.randControl = new boolean[this.populationSize]; 
			// selection
			Chromosome parent1 = this.tournamentSelection(2);
			Chromosome parent2 = this.tournamentSelection(2);
			
			if(this.rand.nextDouble() < this.crossoverProbability) {
				children = this.crossoverOperator.executeCrossover(parent1, parent2);		
			} else {
				children = Arrays.asList(parent1.deepClone(), parent2.deepClone());
			}
			
			OptimizationGenetic.MUTATION_VERIFICATION++;
			if(this.rand.nextDouble() < this.chromossomeMutationProbability) {
				for (int i = 0; i < children.size(); i++) {
					OptimizationGenetic.MUTATION_EXECUTION++;
					children.set(i, this.mutationOperator.mutateGenes(children.get(i), this.geneMutationPercentual));
				}
			}
		
			for (int i = 0; i < children.size(); i++) {
				if(population.size() < this.populationSize) {
					population.add(children.get(i));
				}
			}	
		}
		this.currentPopulation = new ArrayList<>(population);
		
	}
	
	public Chromosome run() {
		int iterNum = 0;
		for( ; iterNum < maxIterations; iterNum++ ) {
			optimizeIteration(iterNum);
		}
		return this.getBestIndividual();
	}
	
}
