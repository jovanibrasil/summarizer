package summ.ai.fuzzy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import summ.utils.Tuple;

public class FuzzySystem {
	
	private FIS fis;
	private FunctionBlock functionBlock;
	
	public FuzzySystem(String fileName) {
		
		this.fis = FIS.load(fileName, true);
		if(this.fis == null) {
			System.err.println("Can't load file: '" + fileName + "'");
	        return;
		}
		this.functionBlock = this.fis.getFunctionBlock(null);
	}
	
	public void showFuzzySystem() {
		JFuzzyChart.get().chart(functionBlock); // show fuzzy system
	}
	
	public void updateSystem() {
		
		// adicionar um bloco de função
		// 
		
		
		//this.fis.addFunctionBlock(name, functionBlock);
		
	}
	
	public static void tipperSystem() {
		String fileName = "flc/tipper.flc";
		Tuple<String, Double> service = new Tuple<>("service", 3.0);
		Tuple<String, Double> food = new Tuple<>("food", 7.0);
		List<Tuple<String, Double>> inputValues = new ArrayList<>(Arrays.asList(service, food));
		String outputVariableName = "tip";
		
		FuzzySystem fs = new FuzzySystem(fileName);
		fs.run(inputValues, outputVariableName);
	}
	
	public static void fb2015System() {
		String fileName = "flc/fb2015.flc";
		Tuple<String, Double> locLen = new Tuple<>("loc_len", 0.5);
		Tuple<String, Double> k1 = new Tuple<>("k1", 0.8);
		Tuple<String, Double> k2 = new Tuple<>("k2", 0.4);
		List<Tuple<String, Double>> inputValues = new ArrayList<>(Arrays.asList(locLen, k1, k2));
		String outputVariableName = "informatividade";
		
		FuzzySystem fs = new FuzzySystem(fileName);
		fs.run(inputValues, outputVariableName);
	}
	
	public Double run(List<Tuple<String, Double>> inputValues, String outputVariableName) {
		
		for (Tuple<String, Double> tuple : inputValues) {
			this.fis.setVariable(tuple.x, tuple.y);
		}

		// evaluate (run the system)
		this.fis.evaluate();
		Variable outputValue = this.functionBlock.getVariable(outputVariableName); // read output variable
		
		// show output 
		//JFuzzyChart.get().chart(outputValue, outputValue.getDefuzzifier(), true); 
		//System.out.println(this.fis); // print fuzzy system configuration 

		return outputValue.getValue();
	}
	
	
	
}
