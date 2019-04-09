package summ.fuzzy.optimization;

import java.util.ArrayList;
import java.util.List;

public class CustomVariable {
	
	private List<CustomLinguisticTerm> linguisticTerms;
	private String name;
	
	public CustomVariable(String name) {
		this.linguisticTerms = new ArrayList<>();
		this.name = name;
	}
	
	public void addLinguisticTerm(CustomLinguisticTerm linguisticTerm) {
		this.linguisticTerms.add(linguisticTerm);
	}
	public List<CustomLinguisticTerm> getLinguisticTerms(){
		return this.linguisticTerms;
	}
	public String getName() {
		return this.name;
	}
	
}
