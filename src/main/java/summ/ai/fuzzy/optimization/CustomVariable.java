package summ.ai.fuzzy.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;

public class CustomVariable {
	
	List<CustomLinguisticTerm> linguisticTerms;
	
	public CustomVariable() {
		this.linguisticTerms = new ArrayList<>();
	}
	public void addLinguisticTerm(CustomLinguisticTerm linguisticTerm) {
		this.linguisticTerms.add(linguisticTerm);
	}
	public List<CustomLinguisticTerm> getLinguisticTerms(){
		return this.linguisticTerms;
	}
	
//	public CustomVariable sum(Object[] linguisticTerm) {
//		CustomVariable customVariable = new CustomVariable();
//		Object[] linguisticTerms = this.linguisticTerms.values().toArray();
//		for (int i = 0; i < linguisticTerms.length; i++) {
//			((CustomLinguisticTerm)linguisticTerms[i]).sum((CustomLinguisticTerm)linguisticTerm[i]);
//		}
//		return customVariable;
//	}
//	
//	public CustomVariable divideBy(Double value) {
//		CustomVariable customVariable = new CustomVariable();
//		Object[] linguisticTerms = this.linguisticTerms.values().toArray();
//		for (int i = 0; i < linguisticTerms.length; i++) {
//			((CustomLinguisticTerm)linguisticTerms[i]).divideBy(value);
//		}
//		return customVariable;
//	}
//	
//	public CustomVariable multiply(Object[] linguisticTerm) {
//		CustomVariable customVariable = new CustomVariable();
//		Object[] linguisticTerms = this.linguisticTerms.values().toArray();
//		for (int i = 0; i < linguisticTerms.length; i++) {
//			((CustomLinguisticTerm)linguisticTerms[i]).multiply((CustomLinguisticTerm)linguisticTerm[i]);
//		}
//		return customVariable;
//	}
//	
//	public CustomVariable sqrt() {
//		CustomVariable customVariable = new CustomVariable();
//		Object[] linguisticTerms = this.linguisticTerms.values().toArray();
//		for (int i = 0; i < linguisticTerms.length; i++) {
//			((CustomLinguisticTerm)linguisticTerms[i]).sqrt();
//		}
//		return customVariable;
//	}
	
	
}
