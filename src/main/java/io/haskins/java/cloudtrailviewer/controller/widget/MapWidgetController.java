package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.FileUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by markhaskins on 10/01/2017.
 */
public class MapWidgetController extends AbstractBaseController {

    private static final String HTML_FILENAME = "geoData.html";

    @FXML private WebView map;
    private WebEngine webEngine;

    @FXML
    private void initialize()
    {
        webEngine = map.getEngine();
    }

    public BorderPane loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/MapWidget.fxml"));
        loader.setController(this);
        try {
            fxmlObject = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fxmlObject;
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService) {

        super.configure(widget, eventTableService);

        map.setPrefHeight(widget.getHeight());
        map.setPrefWidth(widget.getWidth());
    }

    public void newEvents(List<Event> events) {

        for (Event event : events) {
            newEvent(event);
        }
    }

    public void loadingFile(int fileName, int totalFiles) { }

    public void finishedLoading(boolean reload) {

        StringBuilder output = new StringBuilder();

        int highestCount = 0;
        String centerPoint = "";

        for (Map.Entry<String, String> entry : latlngs.entrySet()) {

            List<Event> events = keyValueMap.get(entry.getValue());

            output.append("['").
                    append(entry.getValue()).
                    append(":").
                    append(events.size()).
                    append("',").
                    append(entry.getKey()).
                    append("]");

            output.append(",");

            String cityName = entry.getValue();
            if (cityName != null && cityName.trim().length() > 0) {
                int totalEvents = events.size();

                if (totalEvents > highestCount) {
                    centerPoint = entry.getKey();
                    highestCount = totalEvents;
                }
            }
        }

        String html = getHTML();
        html = html.replaceAll("COORDS", output.toString());
        html = html.replaceAll("CENTER", centerPoint);

        writeGeoDataHtml(html);

        webEngine.load("file://" + FileUtils.getFullPathToFile(HTML_FILENAME));
    }

    private String getHTML() {

        StringBuilder result = new StringBuilder();

        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStreamReader io = new InputStreamReader(classLoader.getResourceAsStream("geodata/GeoIp.html"));


        try( BufferedReader br = new BufferedReader(io) ) {

            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }

        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "Unable to load service APIs", ioe);
        }

        return result.toString();
    }

    private void writeGeoDataHtml(String htmtContent) {

        try {
            File f = new File(FileUtils.getFullPathToFile(HTML_FILENAME));
            if (!f.delete()) {
                LOGGER.log(Level.WARNING, "Failed to delete existing Geo HTML file");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to delete existing Geo HTML file", e);
        }

        FileUtils.writeStringToFile(htmtContent, FileUtils.getFullPathToFile(HTML_FILENAME));

        try (BufferedWriter out = new BufferedWriter(new FileWriter(FileUtils.getFullPathToFile(HTML_FILENAME)))) {
            out.write(htmtContent);
            out.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to create new Geo HTML file", e);
        }
    }
}
