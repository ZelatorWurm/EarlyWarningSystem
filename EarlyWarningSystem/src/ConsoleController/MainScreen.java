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
	
	private JPanelServer jpsElevation;
	private JPanelServer jpsDesertion;
	private JPanelServer jpsAffliction;
	private JPanelServer jpsSerenity;
	
	//General Info
	//Players online graph
	private DefaultCategoryDataset playersOnlineDataSet;
	private ChartPanel playersOnlineChartPanel;
	
	//temp code
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
		jpsElevation = new JPanelServer(Server.Elevation);
		tabbedPane.addTab("Elevation", null, jpsElevation, null);
		//SERENITY
		jpsSerenity = new JPanelServer(Server.Serenity);
		tabbedPane.addTab("Serenity", null, jpsSerenity, null);
		//AFFLICTION
		jpsAffliction = new JPanelServer(Server.Affliction);
		tabbedPane.addTab("Affliction", null, jpsAffliction, null);
		//DESERTION
		jpsDesertion = new JPanelServer(Server.Desertion);
		tabbedPane.addTab("Desertion", null, jpsDesertion, null);
		
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
			
			// refresh elevation panel
			jpsElevation.update();	
			// refresh serenity panel
			jpsSerenity.update();	
			// refresh affliction panel
			jpsAffliction.update();
			// refresh desertion panel
			jpsDesertion.update();
			
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
