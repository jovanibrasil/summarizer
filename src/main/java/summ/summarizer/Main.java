package summ.summarizer;

import java.awt.EventQueue;

import summ.fuzzy.optimization.Optimization;
import summ.gui.SummGUI;
import summ.settings.SummarizerSettings;
import summ.settings.SummarizerSettings.ExecutionType;

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
		
		//invokeGUI();
		
		/*
		 * TODO tratar valores numéricos, datas e dinheiro
		 * TODO tudo que termina sem ponto  desconsiderado, sendo sonsiderado ttulo 
		 * No tf os termos do ttulo no so calculados
		 * 
		 * Overlap - criar forma melhor de adicionar objetos em um map
		 * 
		 * 
		 */
	
		SummarizerSettings summarizerSettings = new SummarizerSettings();
		
		if(summarizerSettings.EXECUTION_TYPE.equals(ExecutionType.OPTIMIZATION)) {
			Optimization optmization = new Optimization(summarizerSettings);
			optmization.run();			
		}else {
			Summarizer summarizer = new Summarizer(summarizerSettings);
			summarizer.run();
		}	
		
	}

}
