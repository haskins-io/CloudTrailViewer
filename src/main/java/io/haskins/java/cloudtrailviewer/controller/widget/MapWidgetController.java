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

package io.haskins.java.cloudtrailviewer.controller.widget;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.service.DataService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.FileUtils;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.lucene.search.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventTarget;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Controller class that provides a Map widget
 *
 * Created by markhaskins on 10/01/2017.
 */
public class MapWidgetController extends AbstractBaseController {

    private static final String HTML_FILENAME = "geoData.html";

    @FXML private WebView map;
    private WebEngine webEngine;

    @FXML
    private void initialize() {

        webEngine = map.getEngine();

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == Worker.State.SUCCEEDED) {

                Document doc = webEngine.getDocument();
                NodeList nList = doc.getElementsByTagName("Div");
                for (int i=0;  i < nList.getLength(); i++) {

                    Node node = nList.item(i);

                    Element element = (Element) node;
                    if (element.getAttribute("class") != null &&
                        element.getAttribute("class").equalsIgnoreCase("city")) {

                        ((EventTarget) element).addEventListener(
                                "click",
                                ev -> {
                                    String cityName = element.getTextContent();

                                    try {
                                        TopDocs result = LuceneUtils.performQuery(this.widget.getType(), widget.getSeriesField(), cityName);
                                        eventTableService.setTableEvents(result, widget.getType());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                },
                                false
                        );
                    }
                }
            }
        });
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

    protected FontAwesomeIconView getWidgetIcon() {
        return new FontAwesomeIconView(FontAwesomeIcon.MAP_MARKER);
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DataService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        map.setPrefHeight(widget.getHeight());
        map.setPrefWidth(widget.getWidth());

        widgetControlsController.hideEditButton();
    }

    public void loadingFile(int fileName, int totalFiles) { }

    public void newEvent(org.apache.lucene.document.Document document) {

        String latLng = "";
        if (this.widget.getSeriesField().startsWith("src") && document.getField("srcLatLng") != null) {
            latLng = document.getField("srcLatLng").stringValue();
        } else if (this.widget.getSeriesField().startsWith("dst") && document.getField("dstLatLng") != null) {
            latLng = document.getField("dstLatLng").stringValue();
        }

        if (latLng != null && latLng.length() > 0) {

            String city;
            if (this.widget.getSeriesField().startsWith("src")) {
                city = document.getField("srcCity").stringValue();
            } else {
                city = document.getField("dstCity").stringValue();
            }

            latlngs.put(latLng, city);

            int count = 0;
            if (keyValueMap.containsKey(latLng)) {
                count = keyValueMap.get(latLng);
            }
            count++;
            keyValueMap.put(latLng, count);
        }
    }

    public void finishedLoading(boolean reload) {

        if (latlngs.isEmpty()) {

            keyValueMap.clear();

            List<org.apache.lucene.document.Document> documents = LuceneUtils.getAllDocuments(this.widget.getType());
            for (org.apache.lucene.document.Document doc : documents) {
                newEvent(doc);
            }
        }

        StringBuilder output = new StringBuilder();

        int highestCount = 0;
        String centerPoint = "";

        for (Map.Entry<String, String> entry : latlngs.entrySet()) {

            int count = keyValueMap.get(entry.getKey());

            output.append("[\"").
                    append(entry.getValue()).
                    append(":").
                    append(count).
                    append("\",").
                    append(entry.getKey()).
                    append("]");

            output.append(",");

            String cityName = entry.getValue();
            if (cityName != null && cityName.trim().length() > 0) {

                if (count > highestCount) {
                    centerPoint = entry.getKey();
                    highestCount = count;
                }
            }
        }

        String html = getHTML();
        html = html.replaceAll("COORDS", output.toString());
        html = html.replaceAll("CENTER", centerPoint);

        writeGeoDataHtml(html);

        String htmlFileUri = new File(FileUtils.getFullPathToFile(HTML_FILENAME)).toURI().toString();
        webEngine.load(htmlFileUri);
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
