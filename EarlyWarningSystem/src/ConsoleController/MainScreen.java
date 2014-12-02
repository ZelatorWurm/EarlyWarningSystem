package ConsoleController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import Model.ModelManager;
import Model.Server;
import Model.PlayersOnline.PlayersOnlineModel;
import Model.PlayersOnline.TimeStampPlayersOnline;

public class MainScreen implements Observer {

	private JFrame frame;
	
	//Players online graph
	private DefaultCategoryDataset dataset;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private final String serie = "First";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
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
	public MainScreen() {
		ModelManager.getModelManager().addObserver(this);
		initialize();
		ModelManager.getModelManager().startRefreshingDataModels();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		dataset = new DefaultCategoryDataset();

		chart = ChartFactory.createBarChart("Players online", // chart title
				"Servers", // domain axis label
				"Number of players", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				false, // include legend
				false, // tooltips?
				false // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);

		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		// OPTIONAL CUSTOMISATION COMPLETED.

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		frame.setContentPane(chartPanel);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//refresh players online graph
		dataset.clear();
		for (Server server : Server.values()) {
			TimeStampPlayersOnline tpo = PlayersOnlineModel.getPlayersOnlineModel().getCurrentPlayersOnlineFromServer(server);
			System.out.println(tpo.getPlayersOnline());
			dataset.addValue(tpo.getPlayersOnline(), serie, Server.getName(server));
		}
		chartPanel.repaint();
	}
}
