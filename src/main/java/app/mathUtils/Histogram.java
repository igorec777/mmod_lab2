package app.mathUtils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class Histogram extends ApplicationFrame {

    public Histogram(Properties properties) {
        super(properties.getApplicationTitle());
        JFreeChart barChart = ChartFactory.createBarChart(
                properties.getChartTitle(),
                properties.getCategory(),
                "Values",
                createDataset(properties),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);

        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }

    private CategoryDataset createDataset(Properties properties) {
        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        int index = properties.getIndex();
        int j = 0;

        for (Double[] data : properties.getResultData()) {
            for (int i = 0; i < data.length - index; i++) {
                dataset.addValue(data[i], properties.getRowKey()[j], String.valueOf(index + i));
                if (index == 0) {
                    dataset.addValue(data[i], properties.getRowKey()[j + 1], String.valueOf(index + i));
                }
            }
            j++;
        }
        return dataset;
    }
}
