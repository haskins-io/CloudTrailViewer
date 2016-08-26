/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2016  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.utils.DateTimeCrosshairLabelGenerator;
import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.thirdparty.WrapLayout;
import com.haskins.cloudtrailviewer.utils.TableUtils;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author mark.haskins
 */
public class MetricsFeature extends BaseFeature implements ActionListener, ChartMouseListener {

    private static final String NAME = "Metrics Feature";
    private static final long serialVersionUID = -3462820837131838769L;


    private static final Map<String, List<Event>> SERVICE_SORTED = new HashMap<>();

    private final Map<Second, List<Event>> secondEvents = new HashMap<>();

    private JToolBar toolbar;
    private JPanel chartCards;
    
    private final TableUtils tableUtils = new TableUtils();

    private ChartPanel chartPanel;
    private Crosshair xCrosshair;

    public MetricsFeature(StatusBar sb, HelpToolBar helpBar) {

        super(
                sb,
                helpBar,
                null,
                new EventTablePanel(EventTablePanel.CHART_EVENT),
                new Help("Metrics Feature", "metrics")
        );

    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public String getIcon() {
        return "Metrics-48.png";
    }

    @Override
    public String getTooltip() {
        return "Metrics Overview";
    }

    @Override
    public String getName() {
        return MetricsFeature.NAME;
    }

    @Override
    public void showPrimaryData(List<Event> events) {
    }

    @Override
    public void reset() {
        
        eventTable.clearEvents();
        eventTable.setVisible(false);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {

        List<Event> allEvents = SERVICE_SORTED.get("ALL");
        if (allEvents == null) {
            allEvents = new ArrayList<>();
            addButton("ALL");
        }
        allEvents.add(event);
        SERVICE_SORTED.put("ALL", allEvents);

        String serviceName = tableUtils.getService(event);
        List<Event> events = SERVICE_SORTED.get(serviceName);
        if (events == null) {
            events = new ArrayList<>();
            addButton(serviceName);
        }
        events.add(event);
        SERVICE_SORTED.put(serviceName, events);
    }

    @Override
    public void finishedLoading() {
        showChart("ALL");
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// ActionListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        showChart(e.getActionCommand());
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// ChartListener methods
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {
        
        try {
            XYItemEntity xyitem = (XYItemEntity)cme.getEntity();
            TimeSeriesCollection dataset = (TimeSeriesCollection)xyitem.getDataset();
            List<TimeSeries> series = dataset.getSeries();
            TimeSeries timeSeries = series.get(xyitem.getSeriesIndex());
            Second second = (Second)timeSeries.getTimePeriod(xyitem.getItem());
                        
            if (!eventTable.isVisible()) {

                jsp.setDividerLocation(0.5);
                jsp.setDividerSize(3);
                eventTable.setVisible(true);
            }

            eventTable.clearEvents();
            eventTable.setEvents(secondEvents.get(second));
            
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Problem responding to Chart click event", e);
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {

        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
        
        this.xCrosshair.setValue(x);
    }


    @Override
    void buildUI() {

        // toolbar
        toolbar = new JToolBar();
        toolbar.setLayout(new WrapLayout(FlowLayout.CENTER, 1, 1));
        toolbar.setFloatable(false);
        toolbar.setBackground(Color.white);
        toolbar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));

        chartCards = new JPanel(new CardLayout());
        eventTable.setVisible(false);

        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, chartCards, eventTable);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().height - jsp.getInsets().bottom - jsp.getDividerSize());
        jsp.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        this.setLayout(new BorderLayout());
        this.add(toolbar, BorderLayout.PAGE_START);
        this.add(jsp, BorderLayout.CENTER);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void showChart(String service) {

        final TimeSeriesCollection chartData = generateTimeSeriesData(service);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(service, "Time", "Calls", chartData, false, true, false);

        
        // draw outter line
        XYLineAndShapeRenderer lineAndShapeRenderer = new XYLineAndShapeRenderer();
        lineAndShapeRenderer.setPaint(new Color(64, 168, 228, 75));
        lineAndShapeRenderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        lineAndShapeRenderer.setSeriesShapesFilled(0, true);
        lineAndShapeRenderer.setSeriesShapesVisible(0, true);
        lineAndShapeRenderer.setUseOutlinePaint(true);
        lineAndShapeRenderer.setUseFillPaint(true);
        
        
        // draw filled area
        XYAreaRenderer renderer = new XYAreaRenderer();
        renderer.setPaint(new Color(64, 168, 228, 50));

        
        // configure Plot
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        plot.setDataset(0, chartData);
        plot.setDataset(1, chartData);

        plot.setRenderer(0, lineAndShapeRenderer);
        plot.setRenderer(1, renderer);
        
        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setUpperMargin(0);
                
        // format chart title
        TextTitle t = chart.getTitle();
        t.setFont(new Font("Arial", Font.BOLD, 14));
        
        
        // Cross Hairs
        xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
        xCrosshair.setLabelVisible(true);
        xCrosshair.setLabelGenerator(new DateTimeCrosshairLabelGenerator());

        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        
        
        // Create the panel
        chartPanel = new ChartPanel(chart);
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(false);
        chartPanel.addChartMouseListener(this);
        chartPanel.addOverlay(crosshairOverlay);

        
        // update the display
        chartCards.removeAll();
        chartCards.add(chartPanel,"");
        chartCards.revalidate();
    }

    private TimeSeriesCollection generateTimeSeriesData(String service) {

        List<Event> serviceEvents = SERVICE_SORTED.get(service);

        Map<Long, List<Event>> tickEvents = new HashMap<>();
        for (Event event : serviceEvents) {

            long second = event.getTimestamp();
            List<Event> events = tickEvents.get(second);
            if (events == null) {
                events = new ArrayList<>();
            }
            events.add(event);
            tickEvents.put(second, events);
        }

        TimeSeries series = new TimeSeries(service);
        
        for (Map.Entry<Long, List<Event>> entry : tickEvents.entrySet()) {
            
            Second secondPeriod = new Second(new Date(entry.getKey()));
            secondEvents.put(secondPeriod, entry.getValue());
            series.add(secondPeriod, entry.getValue().size());
        }
        
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        tsc.addSeries(series);

        return tsc;
    }

    private void addButton(String service) {

        JButton btn = new JButton(service);
        btn.addActionListener(this);
        btn.setActionCommand(service);

        toolbar.add(btn);
    }
}
