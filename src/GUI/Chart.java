package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryStepRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class Chart extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new demo application.
     *
     * @param title  the frame title.
	 * @throws SQLException 
     */
    public Chart(String tablenm, final double[][] data, Comparable<?>[] countries, Comparable<?>[] years) {
        super();

        final CategoryDataset dataset = DatasetUtilities.createCategoryDataset(countries, years, data);

        // create the chart...
        final JFreeChart chart = createChart(dataset,tablenm);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        chartPanel.setEnforceFileExtensions(false);
        
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(chartPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
		);
		setLayout(groupLayout);
        this.add(chartPanel);
    }


    private JFreeChart createChart(final CategoryDataset dataset,String tablenm) {
        final CategoryItemRenderer renderer = new CategoryStepRenderer(true);
        final CategoryAxis domainAxis = new CategoryAxis("Years");
        final ValueAxis rangeAxis = new NumberAxis(tablenm);
        final CategoryPlot plot = new CategoryPlot(dataset, domainAxis, rangeAxis, renderer);
        final JFreeChart chart = new JFreeChart("Country Step Chart", plot);

        chart.setBackgroundPaint(Color.white);
        
        plot.setBackgroundPaint(Color.yellow.darker().darker().darker().darker().darker().darker());
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);
        
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.addCategoryLabelToolTip("Type 1", "The first type.");
        domainAxis.addCategoryLabelToolTip("Type 2", "The second type.");
        domainAxis.addCategoryLabelToolTip("Type 3", "The third type.");
        
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLabelAngle(0 * Math.PI / 2.0);

        return chart;
    }

}