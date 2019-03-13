package summ.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

import java.awt.Color;

public class SummGUI {

	public JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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

	/**
	 * Create the application.
	 */
	public SummGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Texto original:");
		lblNewLabel.setBounds(31, 88, 199, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JTextArea textArea = new JTextArea("Batata não é tomate.");
		textArea.setBounds(30, 115, 938, 622);
		frame.getContentPane().add(textArea);
		JButton btnAnalyze = new JButton("Sumarizar");
		btnAnalyze.setBackground(Color.ORANGE);
		btnAnalyze.setBounds(31, 25, 104, 51);
		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String inputText = textArea.getText();
				
				//TextAnalyzer textAnalyzer = new TextAnalyzer();					
//				String logText = textAnalyzer.analyseText(inputText);
//				logArea.setText(logText);
//				
//				textAnalyzer.spellChecking();
				
				
				// inserir o texto completo no textbox
				// inserir marcações
				// 
				
				try {
				
					String originalText = textArea.getText();
					String highlightText = "não";
					
					Highlighter h = textArea.getHighlighter();
					h.removeAllHighlights();
					int pos = originalText.indexOf(highlightText, 0);  
					h.addHighlight(pos , pos  + highlightText.length(), 
							DefaultHighlighter.DefaultPainter);
					//textArea.setHighlighter(h);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		frame.getContentPane().add(btnAnalyze);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenuItem mntmFile = new JMenuItem("File");
		mntmFile.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mntmFile);
		
		JMenuItem mntmEdit = new JMenuItem("Edit");
		menuBar.add(mntmEdit);
		
		JMenuItem mntmConfigurations = new JMenuItem("Configurations");
		menuBar.add(mntmConfigurations);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		menuBar.add(mntmAbout);
		
	}
}
