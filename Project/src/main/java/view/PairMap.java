package view;

import model.Pair;
import model.SpinfoodEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.util.ShapeUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.labels.XYToolTipGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PairMap extends JFrame {
    private final List<Pair> pairs;

    public PairMap(String title, List<Pair> pairs) {
        super(title);
        this.pairs = pairs;
        JFreeChart chart = createChart(createDataset());
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private XYSeriesCollection createDataset() {
        XYSeries seriesPairs = new XYSeries("Pairs");
        for (Pair p : pairs) {
            seriesPairs.add(p.getKitchen().longitude, p.getKitchen().latitude);
        }

        XYSeries seriesPartyLocation = new XYSeries("Party Location");
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        seriesPartyLocation.add(event.partyLocation.longitude, event.partyLocation.latitude);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesPairs);
        dataset.addSeries(seriesPartyLocation);

        return dataset;
    }

    private JFreeChart createChart(XYSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Pair Locations",
                "Longitude",
                "Latitude",
                dataset,
                PlotOrientation.VERTICAL,
                true,  // include legend
                true,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        XYItemRenderer renderer = plot.getRenderer();

        renderer.setDefaultToolTipGenerator(new XYToolTipGenerator() { //create Tooltip for each Pair and Partylocation Point
            @Override
            public String generateToolTip(XYDataset dataset, int series, int item) {
                if (series == 0) {  // Pairs series
                    Pair p = pairs.get(item);
                    return ("P1: " + p.participant1.name + ", P2: " + p.participant2.name + "; Food preference: " + p.getMainFoodPreference());
                } else {  // Party Location series
                    return "Party Location";
                }
            }
        });

        // Set the shape and color for the pairs
        Shape cross = ShapeUtils.createDiagonalCross(4, 1);
        renderer.setSeriesShape(0, cross);
        renderer.setSeriesPaint(0, Color.blue);

        // Set the shape and color for the party location
        Shape partyLocationShape = ShapeUtils.createRegularCross(6, 2);
        renderer.setSeriesShape(1, partyLocationShape);
        renderer.setSeriesPaint(1, Color.red);

        return chart;
    }

}
