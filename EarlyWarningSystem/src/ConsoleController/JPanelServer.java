package ConsoleController;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import Model.Server;
import Model.PlayersOnline.PlayersOnlineModel;
import Model.PlayersOnline.TimeStampPlayersOnline;

public class JPanelServer extends JPanel{
	private Server server;
	
	private XYSeriesCollection playersOnlineDataSet;
	private ChartPanel playersOnlineChartPanel;
	
	public JPanelServer(Server server) {
		this.server = server;
		setLayout(null);
		
		playersOnlineDataSet = new XYSeriesCollection();
		playersOnlineChartPanel = new ChartPanel(createPlayersOnlineXYChart(playersOnlineDataSet));
		playersOnlineChartPanel.setLayout(null);
		playersOnlineChartPanel.setRangeZoomable(false);
		playersOnlineChartPanel.setPreferredSize(new Dimension(500, 270));
		playersOnlineChartPanel.setDomainZoomable(false);
		playersOnlineChartPanel.setBounds(10, 29, 333, 389);
		add(playersOnlineChartPanel);
		
		JLabel lblPlayersOnline = new JLabel("Players online");
		lblPlayersOnline.setBounds(10, 11, 91, 14);
		add(lblPlayersOnline);
	}
	
	public void update(){
		// refresh players online chart 
		playersOnlineDataSet.removeAllSeries();
		XYSeries series = new XYSeries("First");
		
		ArrayList<TimeStampPlayersOnline> array = null;
		
		if(this.server == Server.Elevation){
			array = PlayersOnlineModel.getPlayersOnlineModel().getPlayersOnlineElevation();
		} else if(this.server == Server.Desertion){
			array = PlayersOnlineModel.getPlayersOnlineModel().getPlayersOnlineDesertion();
		} else if(this.server == Server.Affliction){
			array = PlayersOnlineModel.getPlayersOnlineModel().getPlayersOnlineAffliction();
		} else if(this.server == Server.Serenity){
			array = PlayersOnlineModel.getPlayersOnlineModel().getPlayersOnlineSerenity();
		}
		
		for (int i = 0; i < array.size(); i++) {
			series.add(i * 5, array.get(i).getPlayersOnline());
		}

		playersOnlineDataSet.addSeries(series);
		playersOnlineChartPanel.repaint();
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
}
