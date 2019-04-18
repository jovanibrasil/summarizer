package summ.fuzzy.optimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGenBell;
import net.sourceforge.jFuzzyLogic.membership.Value;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationMethod;
import net.sourceforge.jFuzzyLogic.optimization.Parameter;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import summ.fuzzy.FuzzySystem;
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
    public ErrorFunctionSummarization errorFunction;
    public Random rand;
	
    boolean[] randControl;
    
    List<String> parameters;
    	
	public CustomVariable convertVariableToCustomVariable(Variable variable) {
		CustomVariable cv = new CustomVariable(variable.getName());
		for (Entry<String, LinguisticTerm> linguisticTerm : variable.getLinguisticTerms().entrySet()) {
			MembershipFunction mf = linguisticTerm.getValue().getMembershipFunction();
			CustomLinguisticTerm lt = new CustomLinguisticTerm(mf.getParametersLength(), linguisticTerm.getKey());
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
		System.out.println();
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
			//System.out.println(randIndex);
			if(!randControl[randIndex]) {
				if (bestChromosomeIndex < 0 || this.currentPopulation.get(randIndex).fitness > 
					this.currentPopulation.get(bestChromosomeIndex).fitness) {
					bestChromosomeIndex = randIndex;
				}
				i++;
			}
		}
		randControl[bestChromosomeIndex] = true;
		//System.out.println("Chromossome position " + bestChromosomeIndex + " selected");
		return this.currentPopulation.get(bestChromosomeIndex);
	}
	
	
//	public void setMfParameter(String variableName, String linguisticTermName, 
//			int mfParameterId, double mfParameterValue) {
//	
//		Variable var = this.fuzzyRuleSet.getVariable(variableName);
//		LinguisticTerm lt = var.getLinguisticTerm(linguisticTermName);
//		lt.getMembershipFunction().setParameter(mfParameterId, mfParameterValue);
//		this.setVariable(var.getName(), var);
//		
//	}
	
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
		System.out.println();
	}
	
	public void generateIntermediatePopulation() {
		//System.out.println("Generate intermediate population ...");

		List<Chromosome> population = new ArrayList<>();
		
		
		if(this.elitism) {
			//System.out.println("Elitismo habilitado.");
			Chromosome elite = this.getBestIndividual();
			System.out.println(elite);
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
					children.set(i, this.mutationOperator.executeMutation(children.get(i)));
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
	public OptimizationGenetic(RuleBlock fuzzyRuleSet, ErrorFunctionSummarization errorFunction, ArrayList<Parameter> parameterList,
			Crossover crossoverOperator, List<String> parameters) {
	 	super(fuzzyRuleSet, errorFunction, parameterList);
		
		this.currentPopulation = new ArrayList<>();
		this.populationSize = 10;
		this.crossoverOperator = crossoverOperator;
		this.mutationOperator = new Mutation();
		this.errorFunction = errorFunction;
		this.crossoverProbability = 0.6;
		this.mutationProbability = 0.1;
        this.elitism = true;
		this.rand = new Random();
        this.parameters = parameters;
        
	}
	
	/**
	 * Generate a new population, evaluate and rank the initial population.
	 */
	@Override
	public void optimizeInit() {
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
