package summ.summarizer;

import java.awt.EventQueue;

import summ.gui.SummGUI;

public class Main {

	public static void invokeGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SummGUI window = new SummGUI();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		
		/*
		 * valores numéricos e dinheiro
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		
		//invokeGUI();
		
		//Summarizer.summarizeTexts();
		
		Summarizer.summarizationText("mu94de14-a.txt");
		
		//Summarizer.fuzzyOptimization();
	}

}
