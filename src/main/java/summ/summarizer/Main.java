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
		 * TODO tratar valores numéricos e dinheiro
		 * TODO tudo que termina sem ponto � desconsiderado, sendo sonsiderado t�tulo 
		 * No tf os termos do t�tulo n�o s�o calculados
		 * 
		 * Overlap - criar forma melhor de adicionar objetos em um map
		 * 
		 * 
		 */
		
		//invokeGUI();
		//Summarizer.summarizeTexts();
		Summarizer.summarizationText("mu94de14-a.txt");
		//Summarizer.fuzzyOptimization();
	}

}
