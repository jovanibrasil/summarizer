package model;

public class Word {
	
	private String rawWord;
	
	public Word(String rawWord) {
		this.rawWord = rawWord;
	}
	
	public String getRawWord() {
		return rawWord;
	}

	public void setRawWord(String rawWord) {
		this.rawWord = rawWord;
	}

	@Override
	public String toString() {
		return this.rawWord;
	}
	
}
