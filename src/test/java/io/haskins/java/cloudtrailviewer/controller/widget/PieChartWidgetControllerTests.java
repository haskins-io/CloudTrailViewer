package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.BaseTest;
import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.widget.cloudtrail.ChartPieWidgetController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import javafx.application.Application;
import javafx.scene.chart.PieChart;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by markhaskins on 24/01/2017.
 */
public class PieChartWidgetControllerTests extends BaseTest {

    private AbstractBaseController pieChartController;

    @BeforeClass
    public static void setupClass() throws InterruptedException {

        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(CloudTrailViewer.class);
            }
        };
        t.setDaemon(true);
        t.start();
        Thread.sleep(500);

    }

    @Before
    public void init() {

        pieChartController = new ChartPieWidgetController();
        pieChartController.loadFXML();
    }

    @Test
    public void configureTest() {

        DashboardWidget widget = getTestWidget();

        EventTableService eventTableService = new EventTableService();
        DatabaseService databaseService = new DatabaseService();

        pieChartController.configure(widget, eventTableService, databaseService);

        Class<?> testClass = pieChartController.getClass();

        try {

            Field f = getField(testClass, "pieChart");
            f.setAccessible(true);
            f.get(pieChartController);

            PieChart pieChart = (PieChart)getValueOfField(pieChartController, f);

            assertEquals(640, pieChart.getPrefWidth(), 0);
            assertEquals(320, pieChart.getPrefHeight(), 0);
            assertEquals(widget, pieChartController.getWidget());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assertTrue(true);

    }
}
