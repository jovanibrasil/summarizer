package summ.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Charts extends JFrame {

	private static final long serialVersionUID = 6081226860992968894L;
	private JFreeChart chart;

	public Charts(List<List<Double>> series, int totalIterations, String resultName) {

		XYSeriesCollection dataset = new XYSeriesCollection();
		for (int i = 0; i < series.size(); i++) {
			createDataset(series.get(i), "Serie " + i, dataset);
		}
		
		chart = createChart(resultName, dataset, totalIterations);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);
		add(chartPanel);

		pack();
		setTitle("Line chart");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	public void saveChart(String filePath) {
		try {
			ChartUtilities.saveChartAsPNG(new File(filePath), chart, 450, 400);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private XYDataset createDataset(List<Double> data, String name, XYSeriesCollection dataset) {
		
		XYSeries series = new XYSeries(name);
		for (int i = 0; i < data.size(); i++) {
			series.add(i+1, data.get(i));
		}
		
		dataset.addSeries(series);
		return dataset;
	}

	private JFreeChart createChart(String resultName, XYDataset dataset, int totalIterations) {

		JFreeChart chart = ChartFactory.createXYLineChart("Results: " + resultName, "Iteration", "Evaluation", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		XYPlot plot = chart.getXYPlot();
		
		ValueAxis domainAxis = plot.getDomainAxis();
		ValueAxis rangeAxis = plot.getRangeAxis();
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		domainAxis.setRange(0, totalIterations);
		rangeAxis.setRange(0.0, 1.0);
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);
		chart.getLegend().setFrame(BlockBorder.NONE);

		chart.setTitle(new TextTitle("Results " + resultName, new Font("Serif", java.awt.Font.BOLD, 18)));

		return chart;

	}


}
