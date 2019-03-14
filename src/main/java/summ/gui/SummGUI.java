package summ.gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

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
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mntmFile = new JMenu("File");
		menuBar.add(mntmFile);

		JMenu mntmEdit = new JMenu("Edit");
		menuBar.add(mntmEdit);

		JMenu mntmConfigurations = new JMenu("Configurations");
		menuBar.add(mntmConfigurations);

		JMenu mntmAbout = new JMenu("About");
		menuBar.add(mntmAbout);
		frame.getContentPane().setLayout(null);
		
		
		//frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Texto original:");
		lblNewLabel.setBounds(36, 169, 199, 15);
		frame.getContentPane().add(lblNewLabel);

		JTextArea textArea = new JTextArea("Batata não é tomate. Um tomate é um tomate.") {
			private static final long serialVersionUID = 1L;

			JToolTip toolTip = null;

			@Override
			public JToolTip createToolTip() {
				JToolTip tip = super.createToolTip();
				tip.setBackground(Color.GRAY);
				tip.setForeground(Color.BLACK);
				return tip;
				
				//toolTip = super.createToolTip();
//				if (toolTip == null) {
//					JPanel panel = new JPanel(new GridLayout(1, 0));
//					
//					JLabel label = new JLabel(getToolTipText());
//					panel.add(label);
//					JButton button = new JButton("x");
//					button.addActionListener(new ActionListener() {
//						public void actionPerformed(ActionEvent e) {
//							
//						}
//					});
//					panel.add(button);
//				
//					toolTip = super.createToolTip();
//					toolTip.setLayout(new BorderLayout());
//					Insets insets = toolTip.getInsets();
//					Dimension panelSize = panel.getPreferredSize();
//					panelSize.width += insets.left + insets.right;
//					panelSize.height += insets.top + insets.bottom;
//					toolTip.setPreferredSize(panelSize);
//					toolTip.add(panel);
//				}
//				return toolTip;

			}
		};
		textArea.setBounds(31, 205, 739, 302);
		textArea.setToolTipText("");
		textArea.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					int offset = textArea.viewToModel2D(e.getPoint());

					// System.out.println(offset);

					if (offset >= 0 && offset < 20) {
						textArea.setToolTipText("Primeira frase!");
						//JOptionPane.showMessageDialog(frame, "Primeira frase");
					} else if (offset < 43) {
						textArea.setToolTipText("Segunda frase!");
						//JOptionPane.showMessageDialog(frame, "Segunda frase");
					} else {
						textArea.setToolTipText("");
					}

					// System.out.println(textArea.getText(offset, 3));

				} catch (Exception e2) {
					System.out.println("Erro");
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});

//		long mask = AWTEvent.COMPONENT_EVENT_MASK |
////		      AWTEvent.CONTAINER_EVENT_MASK |
////		      AWTEvent.FOCUS_EVENT_MASK |
////		      AWTEvent.KEY_EVENT_MASK |
////		      AWTEvent.MOUSE_EVENT_MASK |
////		      AWTEvent.MOUSE_MOTION_EVENT_MASK |
//				AWTEvent.WINDOW_EVENT_MASK | AWTEvent.ACTION_EVENT_MASK | AWTEvent.ADJUSTMENT_EVENT_MASK
//				| AWTEvent.ITEM_EVENT_MASK | AWTEvent.TEXT_EVENT_MASK;
//
//		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
//			@Override
//			public void eventDispatched(AWTEvent event) {
//				int id = event.getID();
//				Object source = event.getSource();
//				if (id == 101 && source instanceof JToolTip) {
//					JToolTip tooltip = (JToolTip) source;
//
//					// CustomTooltip tooltip = (CustomTooltip) source;
//
//					System.out.println("" + event.getID() + " " + event.getSource());
//
//				}
//
//			}
//		}, mask);

		frame.getContentPane().add(textArea);
		JButton btnAnalyze = new JButton("Sumarizar");
		btnAnalyze.setToolTipText("teste");
		btnAnalyze.setBackground(Color.ORANGE);
		btnAnalyze.setBounds(35, 16, 109, 52);
		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String inputText = textArea.getText();

				// TextAnalyzer textAnalyzer = new TextAnalyzer();
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
					h.addHighlight(pos, pos + highlightText.length(), DefaultHighlighter.DefaultPainter);
					// textArea.setHighlighter(h);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		frame.getContentPane().add(btnAnalyze);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 10, 10);
		frame.getContentPane().add(panel);

		

	}
}
