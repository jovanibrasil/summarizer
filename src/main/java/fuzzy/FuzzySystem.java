package fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzySystem {
	
	public FuzzySystem() {
	
		//String fileName = "flc/tipper.flc";
		String fileName = "flc/fb2015.flc";
		
		FIS fis = FIS.load(fileName, true);
		
		if(fis == null) {
			System.err.println("Can't load file: '" + fileName + "'");
	        return;
		}
		
		FunctionBlock functionBlock = fis.getFunctionBlock(null);

		JFuzzyChart.get().chart(functionBlock); // show fuzzy system
		
		// set inputs
		//fis.setVariable("service", 3);
		//fis.setVariable("food", 7);
		
		fis.setVariable("loc_len", 0.5);
		fis.setVariable("k1", 0.8);
		fis.setVariable("k2", 0.4);
		

		
		// evaluate (run the system)
		fis.evaluate();
		
		//Variable tip = functionBlock.getVariable("tip"); // read output variable
		Variable tip = functionBlock.getVariable("informatividade"); // read output variable
		
		// show output 
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true); 
		
		System.out.println(fis); // print fuzzy system configuration 
			
		
		
	}
	
	
	
}
