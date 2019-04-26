package summ.fuzzy.optimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenBell;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationMethod;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import summ.fuzzy.FuzzySystem;
import summ.fuzzy.optimization.crossover.CrossoverOperator;
import summ.fuzzy.optimization.evaluation.ErrorFunctionSummarization;
import summ.fuzzy.optimization.functions.FunctionDetails;
import summ.fuzzy.optimization.functions.FunctionFactory;
import summ.fuzzy.optimization.functions.FunctionType;
import summ.fuzzy.optimization.mutation.MutationOperator;

public class OptimizationGenetic extends OptimizationMethod {
	
	private static final Logger log = LogManager.getLogger(OptimizationGenetic.class);
	
	public int populationSize;	
	public List<Chromosome> currentPopulation;
	public double crossoverProbability; // probability of crossover operation
    public double mutationProbability; // probability of mutation operation
    public boolean elitism; 
    
    public CrossoverOperator crossoverOperator;
	public MutationOperator mutationOperator;
    public ErrorFunctionSummarization errorFunction;
    public Random rand;
	
    public int iteration;
    
    boolean[] randControl;
    
    List<String> parameters;
    	
	public CustomVariable convertVariableToCustomVariable(Variable variable) {
		CustomVariable cv = new CustomVariable(variable.getName());
		for (Entry<String, LinguisticTerm> linguisticTerm : variable.getLinguisticTerms().entrySet()) {
			MembershipFunction mf = linguisticTerm.getValue().getMembershipFunction();
			CustomLinguisticTerm lt = new CustomLinguisticTerm(mf.getParametersLength(), 
					linguisticTerm.getKey(), FunctionFactory.generateFunctionInfo(FunctionType.BELL));
			// the membership vector has different order
			// CustomLinguisticTerm: a, b, mean
			// MemberShip: mean, a, b
			lt.setParameter(0, mf.getParameter(1));
			lt.setParameter(1, mf.getParameter(2));
			lt.setParameter(2, mf.getParameter(0));
			
			cv.addLinguisticTerm(lt);
		}
		return cv;
	}
	
	public void generateFirstPopulation() {
		
		log.info("Generating initial population...");
		 
		this.currentPopulation = new ArrayList<>(); // candidate solution matrix
		
		// Create the first Solution. The first Solution is a copy of the initial solution.
		Chromosome chromosome = new Chromosome();
		for (String parameterName : this.parameters) {
			Variable referenceVariable = this.fuzzyRuleSet.getVariable(parameterName);
			chromosome.addGene(convertVariableToCustomVariable(referenceVariable));	
		}
		this.currentPopulation.add(chromosome);
		for (int i = 1; i < this.populationSize; i++) {
			chromosome = new Chromosome();
			for (String parameterName : this.parameters) {	
				Variable referenceVariable = this.fuzzyRuleSet.getVariable(parameterName);
				CustomVariable customVariable = new CustomVariable(referenceVariable.getName());
				for (Entry<String, LinguisticTerm> linguisticTerm : referenceVariable.getLinguisticTerms().entrySet()) {
					CustomLinguisticTerm lt = new CustomLinguisticTerm(3, linguisticTerm.getKey(),
							FunctionFactory.generateFunctionInfo(FunctionType.BELL));
					
					FunctionDetails f = lt.getFunction().getFunctionInfo();
					lt.setParameter(0, mutationOperator.getAleatoryFeasibleCoefficient(f.getRangeMin(0), f.getRangeMax(0)));
					lt.setParameter(1, mutationOperator.getAleatoryFeasibleCoefficient(f.getRangeMin(1), f.getRangeMax(1)));
					lt.setParameter(2, mutationOperator.getAleatoryFeasibleCoefficient(f.getRangeMin(2), f.getRangeMax(2)));
					
					customVariable.addLinguisticTerm(lt);
				}
				chromosome.addGene(customVariable);
			}
			this.currentPopulation.add(chromosome);
		}
	}
	
	/**
	 * Sort the population by fitness.
	 */
	public void rankPopulation() {
		log.info("Ranking population " + this.iteration);
		Collections.sort(this.currentPopulation);
		log.info("Best individual: " + this.getBestIndividual());
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
			for (CustomVariable variable : chromosome.getVariables()) {
				
				FuzzySystem fs = errorFunction.getFuzzySystem();
				Variable var = fs.getVariable(variable.getName());
				
				for (CustomLinguisticTerm term : variable.getLinguisticTerms()) {
					LinguisticTerm lTerm = var.getLinguisticTerm(term.getTermName());
					lTerm.setMembershipFunction(new MembershipFunctionGenBell(
							new Value(term.getParameter(0)), // a
							new Value(term.getParameter(1)), // b
							new Value(term.getParameter(2)))); // mean					
				}
				fs.setVariable(variable.getName(), var);
				
			}	
			chromosome.fitness = errorFunction.evaluate(fuzzyRuleSet);
		}
	}
	
	public void generateIntermediatePopulation() {
		
		log.info("Generate intermediate population");
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
			
			if(this.rand.nextDouble() > this.crossoverProbability) {
				children = this.crossoverOperator.executeCrossover(parent1, parent2);		
			} else {
				children = Arrays.asList(parent1.deepClone(), parent2.deepClone());
			}
			
			if(this.rand.nextDouble() > this.mutationProbability) {
				for (int i = 0; i < children.size(); i++) {
					children.set(i, this.mutationOperator.mutateAllGenes(children.get(i), mutationOperator));
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
	
	/**
	 * Genetic optimization constructor.
	 * 
	 * @param fuzzyRuleSet is the initial fuzzy rule set
	 * 
	 */
	public OptimizationGenetic(RuleBlock fuzzyRuleSet, ErrorFunctionSummarization errorFunction,
			CrossoverOperator crossoverOperator, MutationOperator mutationOperator, double crossoverProbability,
			double mutationProbability, boolean eletism, int populationSize, List<String> parameters) {
		
	 	super(fuzzyRuleSet, errorFunction, null);
		
		this.currentPopulation = new ArrayList<>();
		this.populationSize = populationSize;
		this.crossoverOperator = crossoverOperator;
		this.mutationOperator = mutationOperator;
		this.errorFunction = errorFunction;
		this.crossoverProbability = crossoverProbability;
		this.mutationProbability = mutationProbability;
        this.elitism = eletism;
		this.rand = new Random();
        this.parameters = parameters;
        this.iteration = 0;
        
	}
	
	/**
	 * Generate the first population, evaluate and rank the initial population.
	 */
	@Override
	public void optimizeInit() {
		log.info("Generate the first population, evaluate and rank the initial population.");
		generateFirstPopulation();
		evaluatePopulation();
		rankPopulation();
	}

	@Override
	public void optimizeIteration(int iterationNum) {
		this.iteration++;
		log.info("Iterations:	" + this.iteration + "/" + this.maxIterations);
		generateIntermediatePopulation();				
		evaluatePopulation();
		rankPopulation();	
	}

}
