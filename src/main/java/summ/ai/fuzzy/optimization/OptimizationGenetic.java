package summ.ai.fuzzy.optimization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.optimization.ErrorFunction;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationMethod;
import net.sourceforge.jFuzzyLogic.optimization.Parameter;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import summ.ai.fuzzy.optimization.crossover.Crossover;
import summ.ai.fuzzy.optimization.mutation.Mutation;

public class OptimizationGenetic extends OptimizationMethod {
	
	public int populationSize;	
	public List<Chromosome> currentPopulation;
	public double crossoverProbability; // probability of crossover operation
    public double mutationProbability; // probability of mutation operation
    public boolean elitism; 
    
    public Crossover crossoverOperator;
	public Mutation mutationOperator;
    
	
    public Random rand;
	
	public void setMfParameter(String variableName, String linguisticTermName, 
			int mfParameterId, double mfParameterValue) {
		this.fuzzyRuleSet.getVariable(variableName).getLinguisticTerm(linguisticTermName)
			.getMembershipFunction().setParameter(mfParameterId, mfParameterValue);
	}
	
	public CustomVariable convertVariableToCustomVariable(Variable variable) {
		CustomVariable cv = new CustomVariable();
		for (Entry<String, LinguisticTerm> linguisticTerm : variable.getLinguisticTerms().entrySet()) {
			CustomLinguisticTerm lt = new CustomLinguisticTerm();
			lt.setTermName(linguisticTerm.getKey());
			RealVector parameters = new ArrayRealVector();
			MembershipFunction mf = linguisticTerm.getValue().getMembershipFunction();
			for (int i = 0; i < mf.getParametersLength(); i++) {
				parameters.append(mf.getParameter(i));
			}
			lt.setTermName(mf.getName());
			lt.setParameters(parameters);
			cv.addLinguisticTerm(lt);
		}
		return cv;
	}
	
	public void generateFirstPopulation() {
		System.out.println("Generate initial population ...");
		
		// Instantiate the candidate solution matrix 
		this.currentPopulation = new ArrayList<>();
		FunctionBlock fb = this.fuzzyRuleSet.getFunctionBlock();
		
		// Create the first Solution. The first Solution is a copy of the initial solution.
		Chromosome Solution = new Chromosome();
		for (Parameter parameter : this.parameterList) {
			Variable referenceVariable = this.fuzzyRuleSet.getVariable(parameter.getName());
			Solution.addGene(convertVariableToCustomVariable(referenceVariable));	
		}
		
		for (int i = 1; i < this.populationSize; i++) {
			Solution = new Chromosome();
			for (Parameter parameter : this.parameterList) {	
				CustomVariable variable = new CustomVariable();
				Variable referenceVariable = this.fuzzyRuleSet.getVariable(parameter.getName());
				for (Entry<String, LinguisticTerm> linguisticTerm : referenceVariable.getLinguisticTerms().entrySet()) {
					CustomLinguisticTerm lt = new CustomLinguisticTerm();
					lt.setTermName(linguisticTerm.getKey());		
					lt.parameters.append(BellFunction.getAleatoryFeasibleValue(0));
					lt.parameters.append(BellFunction.getAleatoryFeasibleValue(1));
					lt.parameters.append(BellFunction.getAleatoryFeasibleValue(2));
				}
			}
		}
		System.out.println("End initial population generation ...");
		
	}
	
	/*
	 * Sort the population by fitness.
	 */
	public void rankPopulation() {
		Collections.sort(this.currentPopulation);
	}
	
	/*
	 * Return the best individual. The best individual is the individual with the best
	 * fitness in the current generation. 
	 */
	public Chromosome getBestIndividual() {
		return this.currentPopulation.get(0);
	}
	
	/*
	 * Select a random Solution from the population.
	 */
	public Chromosome randomSelection() {
		Random rand = new Random();
		return this.currentPopulation.get(rand.nextInt(this.populationSize));		
	}
	
	/*
	 * Select a random set of Solutions from the population and return the 
	 * fittest one.
	 */
	public Chromosome tournamentSelection(int tournnamentSize) {
		List<Chromosome> chromossomes = this.selectSolutions(tournnamentSize);
		Collections.sort(chromossomes);
		return this.currentPopulation.get(0);
	}
	 
	/*
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
	
	public void evaluatePopulation() {
		
		for (Chromosome chromosome : currentPopulation) {
			
			
		
			//setMfParameter("k1", "baixo", 0, 2);
			
			
			chromosome.fitness = 0.0;
		}
		
		
	}
	
	public void generateIntermediatePopulation() {
		System.out.println("Generate intermediate population ...");

		List<Chromosome> population = new ArrayList<>();
		
		if(this.elitism) {
			Chromosome elite = this.getBestIndividual();
			population.add(elite);
		}
		
		List<Chromosome> chindren = null;
		while(population.size() < this.populationSize) {
			
			// selection
			Chromosome parent1 = this.tournamentSelection(2);
			Chromosome parent2 = this.tournamentSelection(2);
			
			if(this.rand.nextDouble() > this.crossoverProbability) {
				chindren = this.crossoverOperator.executeCrossover(parent1, parent2);		
			}
			
			if(this.rand.nextDouble() > this.mutationProbability) {
				for (int i = 0; i < chindren.size(); i++) {
					chindren.set(i, this.mutationOperator.executeMutation(chindren.get(i)));
				}
			}
		
			for (int i = 0; i < chindren.size(); i++) {
				if(population.size() < this.populationSize) {
					population.add(chindren.get(i));
				}
			}	
		}
		this.currentPopulation = population;
		
	}
	
	/*
	 * Genetic optimization constructor.
	 * 
	 * @param fuzzyRuleSet is the initial fuzzy rule set
	 * 
	 */
	public OptimizationGenetic(RuleBlock fuzzyRuleSet, ErrorFunction errorFunction, ArrayList<Parameter> parameterList,
			Crossover crossoverOperator) {
		super(fuzzyRuleSet, errorFunction, parameterList);
		
		// function coefficient optimization
		// rules optimization
		this.currentPopulation = new ArrayList<>();
		this.populationSize = 5;
		this.crossoverOperator = crossoverOperator;
		
		this.crossoverProbability = 0.8;
		this.mutationProbability = 0.2;
        this.elitism = true;
		
        this.rand = new Random();
        
		// Generate the initial population
		generateFirstPopulation();
		evaluatePopulation();
		rankPopulation();	
		
	}

	@Override
	public void optimizeIteration(int iterationNum) {
			
		// generate new population
		generateIntermediatePopulation();				
		// TODO set the fuzzyruleset with the new candidate solution
		evaluatePopulation();
		rankPopulation();	
		
	}

}
