package ConsoleController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

import Model.ModelManager;
import Model.RefreshThread;
import Model.Server;
import Model.PlayersOnline.PlayersOnlineModel;
import Model.PlayersOnline.TimeStampPlayersOnline;

import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.SwingConstants;

public class MainScreen implements Observer {
	private JFrame frmEarlyWarningSystem;
	private JLabel lblRefreshing;
	
	//Players online graph
	private DefaultCategoryDataset playersOnlineDataSet;
	private ChartPanel playersOnlineChartPanel;
	
	private XYSeriesCollection playersOnlineElevationDataSet;
	private ChartPanel playersOnlineElevationChartPanel;
	
	private XYSeriesCollection playersOnlineSerenityDataSet;
	private ChartPanel playersOnlineSerenityChartPanel;
	
	private XYSeriesCollection playersOnlineAfflictionDataSet;
	private ChartPanel playersOnlineAfflictionChartPanel;
	
	private XYSeriesCollection playersOnlineDesertionDataSet;
	private ChartPanel playersOnlineDesertionChartPanel;
	
	private JTable playerKillsTable;
	private TableModel playerKillsTableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
					window.frmEarlyWarningSystem.setVisible(true);
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
		frmEarlyWarningSystem = new JFrame();
		frmEarlyWarningSystem.setTitle("Early warning system");
		frmEarlyWarningSystem.setBounds(100, 100, 862, 592);
		frmEarlyWarningSystem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmEarlyWarningSystem.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		playersOnlineDataSet = new DefaultCategoryDataset();	
		
		//GENERAL INFO
		JPanel panel = new JPanel();
		tabbedPane.addTab("General info", null, panel, null);
		panel.setBounds(252, 250, 126, 123);
		panel.setLayout(null);
		
		playersOnlineChartPanel = new ChartPanel(createTotalPlayersChart());
		playersOnlineChartPanel.setBounds(10, 29, 333, 270);
		playersOnlineChartPanel.setPreferredSize(new Dimension(500, 270));
		playersOnlineChartPanel.setLayout(null);
		playersOnlineChartPanel.setPopupMenu(null);
		playersOnlineChartPanel.setDomainZoomable(false);
		playersOnlineChartPanel.setRangeZoomable(false);
		panel.add(playersOnlineChartPanel);
		
		JLabel lblT = new JLabel("Players online");
		lblT.setBounds(10, 11, 91, 14);
		panel.add(lblT);
		
		JLabel label = new JLabel("Table example");
		label.setBounds(371, 11, 119, 14);
		panel.add(label);
		
		playerKillsTableModel = new DefaultTableModel();
		playerKillsTable = createLatestPlayerKillsTable();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(371, 29, 450, 270);
		scrollPane.setViewportView(playerKillsTable);
		panel.add(scrollPane);
		
		//ELEVATION
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Elevation", null, panel_1, null);
		panel_1.setLayout(null);
		
		playersOnlineElevationDataSet = new XYSeriesCollection();
		
		playersOnlineElevationChartPanel = new ChartPanel(createPlayersOnlineXYChart(playersOnlineElevationDataSet));
		playersOnlineElevationChartPanel.setLayout(null);
		playersOnlineElevationChartPanel.setRangeZoomable(false);
		playersOnlineElevationChartPanel.setPreferredSize(new Dimension(500, 270));
		playersOnlineElevationChartPanel.setDomainZoomable(false);
		playersOnlineElevationChartPanel.setBounds(10, 29, 333, 389);
		panel_1.add(playersOnlineElevationChartPanel);
		
		JLabel label_1 = new JLabel("Players online");
		label_1.setBounds(10, 11, 91, 14);
		panel_1.add(label_1);
		
		//SERENITY
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Serenity", null, panel_2, null);
		panel_2.setLayout(null);
		
		playersOnlineSerenityDataSet = new XYSeriesCollection();
		
		playersOnlineSerenityChartPanel = new ChartPanel(createPlayersOnlineXYChart(playersOnlineSerenityDataSet));
		playersOnlineSerenityChartPanel.setLayout(null);
		playersOnlineSerenityChartPanel.setRangeZoomable(false);
		playersOnlineSerenityChartPanel.setPreferredSize(new Dimension(500, 270));
		playersOnlineSerenityChartPanel.setDomainZoomable(false);
		playersOnlineSerenityChartPanel.setBounds(10, 29, 333, 389);
		panel_2.add(playersOnlineSerenityChartPanel);
		
		JLabel label_2 = new JLabel("Players online");
		label_2.setBounds(10, 11, 91, 14);
		panel_2.add(label_2);
		
		//AFFLICTION
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Affliction", null, panel_3, null);
		panel_3.setLayout(null);
		
		playersOnlineAfflictionDataSet = new XYSeriesCollection();
		
		playersOnlineAfflictionChartPanel = new ChartPanel(createPlayersOnlineXYChart(playersOnlineAfflictionDataSet));
		playersOnlineAfflictionChartPanel.setLayout(null);
		playersOnlineAfflictionChartPanel.setRangeZoomable(false);
		playersOnlineAfflictionChartPanel.setPreferredSize(new Dimension(500, 270));
		playersOnlineAfflictionChartPanel.setDomainZoomable(false);
		playersOnlineAfflictionChartPanel.setBounds(10, 29, 333, 389);
		panel_3.add(playersOnlineAfflictionChartPanel);
		
		JLabel label_3 = new JLabel("Players online");
		label_3.setBounds(10, 11, 91, 14);
		panel_3.add(label_3);
		
		//DESERTION
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Desertion", null, panel_4, null);
		panel_4.setLayout(null);
		
		playersOnlineDesertionDataSet = new XYSeriesCollection();
		
		playersOnlineDesertionChartPanel = new ChartPanel(createPlayersOnlineXYChart(playersOnlineDesertionDataSet));
		playersOnlineDesertionChartPanel.setLayout(null);
		playersOnlineDesertionChartPanel.setRangeZoomable(false);
		playersOnlineDesertionChartPanel.setPreferredSize(new Dimension(500, 270));
		playersOnlineDesertionChartPanel.setDomainZoomable(false);
		playersOnlineDesertionChartPanel.setBounds(10, 29, 333, 389);
		panel_4.add(playersOnlineDesertionChartPanel);
		
		JLabel label_4 = new JLabel("Players online");
		label_4.setBounds(10, 11, 91, 14);
		panel_4.add(label_4);
		
		lblRefreshing = new JLabel("Refreshing..");
		lblRefreshing.setHorizontalAlignment(SwingConstants.RIGHT);
		frmEarlyWarningSystem.getContentPane().add(lblRefreshing, BorderLayout.SOUTH);
				
	}
	
	@Override
	public void update(Observable arg0, Object arg) {
		if(arg != null && arg instanceof String && !((String) arg).isEmpty()){
			lblRefreshing.setText("Refreshing: "+100/RefreshThread.SLEEP_TIMER*ModelManager.getModelManager().getProgress() + "%");
		} else {
			//refresh players online graph
			playersOnlineDataSet.clear();
			for (Server server : Server.values()) {
				TimeStampPlayersOnline tpo = PlayersOnlineModel.getPlayersOnlineModel().getCurrentPlayersOnlineFromServer(server);
				System.out.println(tpo.getPlayersOnline());
				playersOnlineDataSet.addValue(tpo.getPlayersOnline(), "First", Server.getName(server));
			}
			playersOnlineChartPanel.repaint();
	
			//refresh players online chart elevation
			playersOnlineElevationDataSet.removeAllSeries();
			XYSeries series1 = new XYSeries("First");
			
			ArrayList<TimeStampPlayersOnline> elevationArray = PlayersOnlineModel.getPlayersOnlineModel().getPlayersOnlineElevation();
			
			for (int i = 0; i < elevationArray.size(); i++) {
				series1.add(i*5, elevationArray.get(i).getPlayersOnline());
			}
	
			playersOnlineElevationDataSet.addSeries(series1);
			playersOnlineElevationChartPanel.repaint();
			
			// refresh players online chart serenity
			playersOnlineSerenityDataSet.removeAllSeries();
			XYSeries series2 = new XYSeries("First");
	
			ArrayList<TimeStampPlayersOnline> serenityArray = PlayersOnlineModel
					.getPlayersOnlineModel().getPlayersOnlineSerenity();
	
			for (int i = 0; i < serenityArray.size(); i++) {
				series2.add(i * 5, serenityArray.get(i).getPlayersOnline());
			}
	
			playersOnlineSerenityDataSet.addSeries(series2);
			playersOnlineSerenityChartPanel.repaint();
	
			// refresh players online chart affliction
			playersOnlineAfflictionDataSet.removeAllSeries();
			XYSeries series3 = new XYSeries("First");
	
			ArrayList<TimeStampPlayersOnline> afflictionArray = PlayersOnlineModel
					.getPlayersOnlineModel().getPlayersOnlineAffliction();
	
			for (int i = 0; i < afflictionArray.size(); i++) {
				series3.add(i * 5, afflictionArray.get(i).getPlayersOnline());
			}
	
			playersOnlineAfflictionDataSet.addSeries(series3);
			playersOnlineAfflictionChartPanel.repaint();
	
			// refresh players online chart desertion
			playersOnlineDesertionDataSet.removeAllSeries();
			XYSeries series4 = new XYSeries("First");
	
			ArrayList<TimeStampPlayersOnline> desertionArray = PlayersOnlineModel
					.getPlayersOnlineModel().getPlayersOnlineDesertion();
	
			for (int i = 0; i < desertionArray.size(); i++) {
				series4.add(i * 5, desertionArray.get(i).getPlayersOnline());
			}
	
			playersOnlineDesertionDataSet.addSeries(series4);
			playersOnlineDesertionChartPanel.repaint();
			
			//refresh player kills
			//playerKillsTableModel.setValueAt(aValue, rowIndex, columnIndex);
		}
	}
	
	private JTable createLatestPlayerKillsTable(){
		String[] columnNames = { "Slayer", "Killed", "Date","Server"};
		String[][] data = new String[0][0];
		
		JTable table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.setModel(playerKillsTableModel);
		
		return table;
	}
	
	private JFreeChart createTotalPlayersChart(){
		JFreeChart chart = ChartFactory.createBarChart(null, // chart title
				null, // domain axis label
				null, // range axis label
				playersOnlineDataSet, // data
				PlotOrientation.VERTICAL, // orientation
				false, // include legend
				false, // tooltips?
				false // URLs?
				);

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

		// set the bar colors
		final CategoryItemRenderer renderer = new CustomRenderer(
	            new Paint[] {Color.yellow, Color.green, Color.decode("#4B0082"),Color.red}
	        );
		
		renderer.setItemLabelsVisible(true);
		
		// text properties
        final ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
        );
        
        // some final setters
		renderer.setPositiveItemLabelPosition(p);
		renderer.setItemLabelsVisible(false);
		
		plot.setRenderer(renderer);
		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions
				.createUpRotationLabelPositions(Math.PI / 6.0));
		
		return chart;
	}

	private JFreeChart createPlayersOnlineXYChart(XYSeriesCollection  dataset) {

		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(
				null, // chart title
				"Minutes ago", // x axis label
				null, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, false, // include legend
				false, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(1, false);
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}
	
	class CustomRenderer extends BarRenderer {

        private Paint[] colors;

        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }
}
