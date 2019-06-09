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
import summ.fuzzy.optimization.settings.OptimizationSettings;

public class GeneticOptimization {
	
	private static final Logger log = LogManager.getLogger(GeneticOptimization.class);
	
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
    
	
	public double lastEvaluationResult = 0.0;
	public int convergenceCounter = 0;
	
	private int maxGenerations;
    
    /**
	 * Genetic optimization constructor.
	 * 
	 * @param fuzzyRuleSet is the initial fuzzy rule set
	 * 
	 */
	public GeneticOptimization(ErrorFunctionSummarization errorFunction, OptimizationSettings optSettings, FuzzySystem fuzzySystem) {
		
		this.errorFunction = errorFunction;
		
		this.currentPopulation = new ArrayList<>();
		this.populationSize = optSettings.POPULATION_SIZE;
		this.crossoverOperator = optSettings.CROSSOVER_OPERATOR;
		this.mutationOperator = optSettings.MUTATION_OPERATOR;
		this.crossoverProbability = optSettings.CROSSOVER_PROBABILITY;
		this.chromossomeMutationProbability = optSettings.MUTATION_PROBABILITY;
        this.geneMutationPercentual = optSettings.GENE_MUTATION_PERCENTUAL;
		this.elitism = optSettings.ELITISM;
        this.parameters = optSettings.VAR_NAMES;
        this.maxGenerations = optSettings.MAX_ITERATIONS;

        this.fuzzySystem = fuzzySystem;
        
        this.iteration = 0;
		this.rand = new Random();
        this.bestFitnessSerie = new ArrayList<>();
        this.averageFitnessSerie = new ArrayList<>();
        this.worstFitnessSerie = new ArrayList<>();
        
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
		log.trace("Mutation verification: " + GeneticOptimization.MUTATION_VERIFICATION);
		log.trace("Mutation execution: " + GeneticOptimization.MUTATION_EXECUTION);
		log.trace("Mutation operator hits: " + MutationOperator.VALUE_GEN_HIT);
		log.trace("Mutation operator errors: " + MutationOperator.VALUE_GEN_ERROR);
	}

	public void optimizeIteration(int iterationNum) {
		
		this.iteration++;
		log.info("Generation:	" + this.iteration + "/" + this.maxGenerations);
		generateIntermediatePopulation();				
		evaluatePopulation();
		rankPopulation();	
		log.trace("Mutation verification: " + GeneticOptimization.MUTATION_VERIFICATION);
		log.trace("Mutation execution: " + GeneticOptimization.MUTATION_EXECUTION);
		log.trace("Mutation operator hits: " + MutationOperator.VALUE_GEN_HIT);
		log.trace("Mutation operator errors: " + MutationOperator.VALUE_GEN_ERROR);
		
	}
    
	public void generateFirstPopulation() {
		
		log.debug("Generating initial population ...");
		 
		this.currentPopulation = new ArrayList<>(); // candidate solution matrix
		
		// Create the first Solution. The first Solution is a copy of the initial solution.
		Chromosome chromosome = new Chromosome();
//		chromosome.setGenes(fuzzySystem.getCoefficients());
//		log.debug("Seeding: " + chromosome);
//		this.currentPopulation.add(chromosome);
		
		MutationOperator fpMutator = new UniformMutation(new BellFunction());
		
		for (int i = 0; i < this.populationSize; i++) {
			chromosome = new Chromosome();
			int cIndex = 1;
			for (int j = 0; j < fuzzySystem.getCoefficients().getDimension(); j+=3) {
				chromosome.setGene(j, fpMutator.getAleatoryFeasibleCoefficient(0));
				chromosome.setGene(j+1, fpMutator.getAleatoryFeasibleCoefficient(1));
				double min, max;
				if(cIndex == 1) {
					min = 0.0; max = 0.3333;
				}else if(cIndex == 2) {
					min = 0.3334; max = 0.6666;
				}else {
					min = 0.6667; max = 1.0;
					cIndex = 0;
				}
				cIndex++;
				chromosome.setGene(j+2, fpMutator.getAleatoryFeasibleCoefficient(min, max));
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
		
//	 	int duplicatedCounter = countDuplicates(this.currentPopulation);
//		log.info("Current population has " + duplicatedCounter + " duplicated chromosomes ...");
		
		log.debug("Ranking population " + this.iteration);
		Collections.sort(this.currentPopulation);
		
//		for (int i = 0; i < this.currentPopulation.size(); i++) {
//			System.out.println("[" + i + "] fitness: " +  this.currentPopulation.get(i).fitness);
//		}
		
		double averageFitness = this.currentPopulation.stream()
				.mapToDouble(x -> x.fitness).sum() / this.currentPopulation.size();
		this.bestFitnessSerie.add(this.getBestIndividual().fitness);
		//this.worstFitnessSerie.add(this.getWorstIndividual().fitness);
		this.averageFitnessSerie.add(averageFitness);
		log.info("Best fitness: " + this.getBestIndividual().fitness);
		
		log.debug("Average fitness: " + averageFitness);
		log.debug("Worst fitness: " + this.getWorstIndividual().fitness);
		log.debug("Best individual: " + this.getBestIndividual());
		log.trace("Crossover execution counter: " + GeneticOptimization.CROSSOVER_COUNTER);
		GeneticOptimization.CROSSOVER_COUNTER = 0;
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
		return this.currentPopulation.get(rand.nextInt(this.currentPopulation.size()));		
	}
	
	/**
	 * Select a random set of Solutions from the population and return the 
	 * fittest one. Randomically select a set of Solutions from the population.
	 */
	public Chromosome tournamentSelection(int tournnamentSize) {
		int bestChromosomeIndex = -1;
				
		int i = 0;
		while (i < tournnamentSize) {
			int randIndex = rand.nextInt(this.currentPopulation.size());
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
		
		List<Chromosome> population = new ArrayList<>();
		population.add(this.currentPopulation.remove(0).deepClone());
		
//		for (Chromosome chromosome : currentPopulation) {
//			if(!chromosome.evaluate) continue;
//				
//			this.fuzzySystem.setCoefficients(chromosome.getGenes());
//			chromosome.fitness = errorFunction.evaluate(this.fuzzySystem);
//			chromosome.evaluate = false;
//		}
		
		// avalia primeiro as funções em relação conformidade das funções
		for (Chromosome chromosome : currentPopulation) {
			chromosome.fitness = errorFunction.f(chromosome.getGenes());
		}
		
		// pega um percentual das melhores e avalia de acordo com a função de avaliação
		Collections.sort(this.currentPopulation);
		int maxChromossomes = (int)(currentPopulation.size() * 0.8);
		
		for (Chromosome chromosome : currentPopulation) {
			//if(!chromosome.evaluate) continue;
				
			this.fuzzySystem.setCoefficients(chromosome.getGenes());
			chromosome.fitness += errorFunction.evaluate(this.fuzzySystem);
			//chromosome.evaluate = false;
			population.add(chromosome.deepClone());
			
			if(population.size() == maxChromossomes) {
				break;
			}
			
		}
		this.currentPopulation = new ArrayList<>(population);
		Collections.sort(this.currentPopulation);
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
				GeneticOptimization.CROSSOVER_COUNTER++;
			} else {
				children = Arrays.asList(parent1.deepClone(), parent2.deepClone());
			}
			
			GeneticOptimization.MUTATION_VERIFICATION++;
			if(this.rand.nextDouble() < this.chromossomeMutationProbability) {
				for (int i = 0; i < children.size(); i++) {
					GeneticOptimization.MUTATION_EXECUTION++;
					children.set(i, this.mutationOperator.mutateGenes(children.get(i), this.geneMutationPercentual));
					children.get(i).evaluate = true;
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
		for( ; iterNum < maxGenerations; iterNum++ ) {
			if(this.convergenceCounter < 5) {
				optimizeIteration(iterNum);
				Chromosome c = this.getBestIndividual();
				if(this.lastEvaluationResult == c.fitness) {
					this.convergenceCounter++;
				}else {
					this.convergenceCounter = 0;
				}
				this.lastEvaluationResult = c.fitness;	
			}else {
				log.info("The optimization was stopped by convergence.");
				break;
			}
		}
		return this.getBestIndividual();
	}
	
}
