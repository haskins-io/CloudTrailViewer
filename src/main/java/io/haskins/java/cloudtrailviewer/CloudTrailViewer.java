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

package io.haskins.java.cloudtrailviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * Entry point to application.
 *
 * Created by markhaskins on 03/01/2017.
 */
@SpringBootApplication
public class CloudTrailViewer extends Application {

    public static final String APP_NAME = "CloudTrail Viewer";

    private static final ConfigurableApplicationContext springContext = SpringApplication.run(CloudTrailViewer.class);

    private BorderPane rootPane;

    public static void main(final String[] args) {
        Application.launch(args);
    }
    @Override
    public void init() throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootPane = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("/style/cloudtrailviewer.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("CloudTrail Viewer");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }
}
