package summ.fuzzy.optimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.optimization.ErrorFunction;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationMethod;
import net.sourceforge.jFuzzyLogic.optimization.Parameter;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import summ.fuzzy.optimization.crossover.Crossover;
import summ.fuzzy.optimization.mutation.Mutation;

public class OptimizationGenetic extends OptimizationMethod {
	
	public int populationSize;	
	public List<Chromosome> currentPopulation;
	public double crossoverProbability; // probability of crossover operation
    public double mutationProbability; // probability of mutation operation
    public boolean elitism; 
    
    public Crossover crossoverOperator;
	public Mutation mutationOperator;
    
    public Random rand;
	
    List<String> parameters;
    	
	public CustomVariable convertVariableToCustomVariable(Variable variable) {
		CustomVariable cv = new CustomVariable(variable.getName());
		for (Entry<String, LinguisticTerm> linguisticTerm : variable.getLinguisticTerms().entrySet()) {
			MembershipFunction mf = linguisticTerm.getValue().getMembershipFunction();
			CustomLinguisticTerm lt = new CustomLinguisticTerm(mf.getParametersLength(), linguisticTerm.getKey());
			for (int i = 0; i < mf.getParametersLength(); i++) {
				lt.setParameter(i, mf.getParameter(i));
			}
			cv.addLinguisticTerm(lt);
		}
		return cv;
	}
	
	public void generateFirstPopulation() {
		System.out.println("Generate initial population ...");
		
		// Instantiate the candidate solution matrix 
		this.currentPopulation = new ArrayList<>();
		
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
					CustomLinguisticTerm lt = new CustomLinguisticTerm(3, linguisticTerm.getKey());
					lt.setParameter(0, BellFunction.getAleatoryFeasibleValue(0));
					lt.setParameter(1, BellFunction.getAleatoryFeasibleValue(1));
					lt.setParameter(2, BellFunction.getAleatoryFeasibleValue(2));
					customVariable.addLinguisticTerm(lt);
				}
				chromosome.addGene(customVariable);
			}
			this.currentPopulation.add(chromosome);
		}
		System.out.println("End initial population generation ...");
		
	}
	
	/**
	 * Sort the population by fitness.
	 */
	public void rankPopulation() {
		Collections.sort(this.currentPopulation);
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
	 * fittest one.
	 */
	public Chromosome tournamentSelection(int tournnamentSize) {
		List<Chromosome> chromossomes = this.selectSolutions(tournnamentSize);
		Collections.sort(chromossomes);
		return this.currentPopulation.get(0);
	}
	 
	/**
	 * Randomically select a set of Solutions from the population.
	 */
	public List<Chromosome> selectSolutions(int selectionSize) {
		System.out.println("Selection ...");	
		// tournament selection
		List<Chromosome> selectedSolutions = new ArrayList<>();
		boolean[] randControl = new boolean[this.populationSize];
		int i = 0;
		while (i < selectionSize) {
			Random rand = new Random();
			int randIndex = rand.nextInt(this.populationSize);
			if(!randControl[randIndex]) {
				selectedSolutions.add(this.currentPopulation.get(randIndex));
				randControl[randIndex] = true;
				i++;
			}
		}
		return selectedSolutions;
	}
	
	public void setMfParameter(String variableName, String linguisticTermName, 
			int mfParameterId, double mfParameterValue) {
		this.fuzzyRuleSet.getVariable(variableName).getLinguisticTerm(linguisticTermName)
			.getMembershipFunction().setParameter(mfParameterId, mfParameterValue);
	}
	
	public void evaluatePopulation() {
		
		for (Chromosome chromosome : currentPopulation) {
			for (CustomVariable variable : chromosome.getVariables()) {
				for (CustomLinguisticTerm term : variable.getLinguisticTerms()) {
					for (int index = 0; index < term.getParametersLength(); index++) {
						this.setMfParameter(
								variable.getName(), 
								term.getTermName(), 
								index,
								term.getParameter(0));		
					}
				}
			}	
			chromosome.fitness = errorFunction.evaluate(fuzzyRuleSet);
		}
		
	}
	
	public void generateIntermediatePopulation() {
		System.out.println("Generate intermediate population ...");

		List<Chromosome> population = new ArrayList<>();
		
		if(this.elitism) {
			Chromosome elite = this.getBestIndividual();
			population.add(elite);
		}
		
		List<Chromosome> children = null;
		while(population.size() < this.populationSize) {
			
			// selection
			Chromosome parent1 = this.tournamentSelection(2);
			Chromosome parent2 = this.tournamentSelection(2);
			
			if(this.rand.nextDouble() > this.crossoverProbability) {
				System.out.println("crossover ...");
				children = this.crossoverOperator.executeCrossover(parent1, parent2);		
			}else {
				children = Arrays.asList(parent1, parent2);
			}
			
			if(this.rand.nextDouble() > this.mutationProbability) {
				for (int i = 0; i < children.size(); i++) {
					children.set(i, this.mutationOperator.executeMutation(children.get(i)));
				}
			}
		
			for (int i = 0; i < children.size(); i++) {
				if(population.size() < this.populationSize) {
					population.add(children.get(i));
				}
			}	
		}
		this.currentPopulation = population;
		
	}
	
	/**
	 * Genetic optimization constructor.
	 * 
	 * @param fuzzyRuleSet is the initial fuzzy rule set
	 * 
	 */
	public OptimizationGenetic(RuleBlock fuzzyRuleSet, ErrorFunction errorFunction, ArrayList<Parameter> parameterList,
			Crossover crossoverOperator, List<String> parameters) {
	 	super(fuzzyRuleSet, errorFunction, parameterList);
		
		// function coefficient optimization
		// rules optimization
		this.currentPopulation = new ArrayList<>();
		this.populationSize = 5;
		this.crossoverOperator = crossoverOperator;
		this.mutationOperator = new Mutation();
		
		this.crossoverProbability = 0.8;
		this.mutationProbability = 0.2;
        this.elitism = true;
		
        this.rand = new Random();
        
        this.parameters = parameters;
        
		// Generate, evaluate and rank the initial population
		generateFirstPopulation();
		evaluatePopulation();
		rankPopulation();	
		
	}

	@Override
	public void optimizeIteration(int iterationNum) {
		// generate new population
		generateIntermediatePopulation();				
		evaluatePopulation();
		rankPopulation();	
	}

}
