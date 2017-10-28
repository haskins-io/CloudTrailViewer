/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.controller.widget.WidgetListener;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.utils.FileUtils;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Dashboard service is responsible for loading a dashboard definition file, instantiating the appropriate widgets
 * and adding them to the screen.
 *
 * Created by markhaskins on 03/01/2017.
 */
@Service
public class DashboardService implements WidgetListener {

    private final static Logger LOGGER = Logger.getLogger("DashboardService");

    private static final String DASHBOARD_EXT = ".ctd";
    private static final String DEFAULT_DASHBOARD = "default" + DASHBOARD_EXT;

    private final static String WIDGET_CONTROLLER_PACKAGE = "io.haskins.java.cloudtrailviewer.controller.widget";

    private Pane pane;
    private String dashboardName;

    private final Map<String, List<DashboardWidget>> widgets = new HashMap<>();

    private final EventService eventService;
    private final VpcFlowLogService vpcFlowLogService;
    private final ElbLogService elbLogService;

    private final EventTableService eventTableService;
    private final DatabaseService databaseService;

    @Autowired
    public DashboardService(
            EventService eventService, VpcFlowLogService vpcFlowLogService, ElbLogService elbLogService,
            EventTableService eventTableService, DatabaseService databaseService
    )
    {
        widgets.put("widgets", new ArrayList<>());

        this.eventService = eventService;
        this.vpcFlowLogService = vpcFlowLogService;
        this.elbLogService = elbLogService;

        this.eventTableService = eventTableService;
        this.databaseService = databaseService;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void init(Pane pane) {
        this.pane = pane;
        initDefaultDashboard();
    }

    public void loadDashboard(String name) {

        dashboardName = name;

        List<DashboardWidget> widgets = loadDashboardFile(name);

        for (DashboardWidget widget : widgets) {

            if (widget.getType().equalsIgnoreCase("cloudtrail")) {
                addWidgetToDashboard(widget, this.eventService);

            } else if (widget.getType().equalsIgnoreCase("vpclogs")) {
                addWidgetToDashboard(widget, this.vpcFlowLogService);

            } else if (widget.getType().equalsIgnoreCase("elblogs")) {
                addWidgetToDashboard(widget, this.elbLogService);
            }

        }
    }

    public void addWidgetToDashboard(DashboardWidget widget, DataService dataService) {

        Platform.runLater(() -> {

            StringBuilder widget_class_name = new StringBuilder()
                    .append(WIDGET_CONTROLLER_PACKAGE)
                    .append(".")
                    .append(widget.getWidget())
                    .append("WidgetController");

            try {
                Class widget_class = Class.forName(widget_class_name.toString());
                Constructor c = widget_class.getConstructor();
                AbstractBaseController controller = (AbstractBaseController)c.newInstance();
                pane.getChildren().add(controller.loadFXML());

                controller.configure(widget, eventTableService, dataService);
                controller.addWidgetListener(this);

//                dataService.registerAsListener(controller);
//                dataService.injectEvents(controller);
                controller.finishedLoading(true);

                List<DashboardWidget> widgetsList = widgets.get("widgets");
                widgetsList.add(widget);

            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                LOGGER.log(Level.WARNING, "Exception caught adding Widget", e);
            }
        });
    }

    public void saveDashboard() {
        Gson g = new Gson();

        String dashboardJson = g.toJson(widgets);
        String filename = FileUtils.getFullPathToFile(dashboardName + DASHBOARD_EXT);

        FileUtils.writeStringToFile(dashboardJson, filename);
    }

    public void newDashboard(String name) {

        dashboardName = name;

        clearDownDashboard();
    }

    public void openDashboard(String name) {

        clearDownDashboard();
        loadDashboard(name);
    }

    public String getCurrentDashboardName() {
        return this.dashboardName;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void clearDownDashboard() {

        widgets.put("widgets", new ArrayList<>());
        pane.getChildren().clear();
    }

    private List<DashboardWidget> loadDashboardFile(String name) {

        Gson g = new Gson();
        List<DashboardWidget> widgets = new ArrayList<>();

        try {
            String contents = FileUtils.getFileAsString(FileUtils.getFullPathToFile(name + DASHBOARD_EXT));

            JsonObject jsonObject = new JsonParser().parse(contents).getAsJsonObject();
            JsonArray jsonWidgets = (JsonArray) jsonObject.get("widgets");

            for (Object widget : jsonWidgets) {

                try {
                    JsonObject obj = (JsonObject) widget;
                    DashboardWidget e = g.fromJson(obj, DashboardWidget.class);
                    e.validate();

                    widgets.add(e);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Create Event from JSON : ", e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return widgets;
    }

    private void initDefaultDashboard() {

        if (!FileUtils.doesFileExist( FileUtils.getFullPathToFile(DEFAULT_DASHBOARD))) {

            // make file from resource version
            StringBuilder defaultResource = new StringBuilder();

            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStreamReader io = new InputStreamReader(classLoader.getResourceAsStream("dashboard/default.json"));

            try (BufferedReader br = new BufferedReader(io)) {

                String line;
                while ((line = br.readLine()) != null) {
                    defaultResource.append(line).append("\n");
                }

            } catch (IOException ioe) {
                LOGGER.log(Level.WARNING, "Unable to read default dashboard from resource bundle", ioe);
            }

            FileUtils.writeStringToFile(defaultResource.toString(), FileUtils.getFullPathToFile(DEFAULT_DASHBOARD));
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// WidgetListener methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void removeWidget(AbstractBaseController controller) {

        pane.getChildren().remove(controller.getFXMLObject());

        List<DashboardWidget> widgetsList = widgets.get("widgets");
        widgetsList.remove(controller.getWidget());
    }
}
