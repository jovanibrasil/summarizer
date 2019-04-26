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
		 * TODO tratar valores numéricos, datas e dinheiro
		 * TODO tudo que termina sem ponto  desconsiderado, sendo sonsiderado ttulo 
		 * No tf os termos do ttulo no so calculados
		 * 
		 * Overlap - criar forma melhor de adicionar objetos em um map
		 * 
		 * 
		 */
		
		//invokeGUI();
		//Summarizer.summarizeTexts();
//		Summarizer.summarizationText("ce94ab10-a.txt", "fcl/var0.fcl");
//		Summarizer.summarizationText("ce94ab10-a.txt", "fcl/var1.fcl");
		Summarizer.fuzzyOptimization();
	}

}
