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

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 *
 * Entry point to application.
 *
 * The splash page logic is based on an example found here : https://gist.github.com/jewelsea/2305098
 *
 * Created by markhaskins on 03/01/2017.
 */
@SpringBootApplication
public class CloudTrailViewer extends Application {

    private Pane splashLayout;

    private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 227;

    private ConfigurableApplicationContext springContext;

    private BorderPane rootPane;

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {

        Image image = new Image("/images/splash.png");
        ImageView splash = new ImageView(image);

        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash);
        splashLayout.setEffect(new DropShadow());

    }

    @Override
    public void start(Stage stage) throws Exception {

        final Task<Void> friendTask = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException, IOException {

                springContext = SpringApplication.run(CloudTrailViewer.class);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                fxmlLoader.setControllerFactory(springContext::getBean);
                rootPane = fxmlLoader.load();

                return null;
            }
        };

        showSplash(stage, friendTask, this::showMainStage);
        new Thread(friendTask).start();
    }

    private void showMainStage() {

        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("/style/cloudtrailviewer.css").toExternalForm());

        Stage mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setScene(scene);
        mainStage.setTitle("CloudTrail Viewer");
        mainStage.show();
    }

    private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {

        task.stateProperty().addListener((observableValue, oldState, newState) -> {

            if (newState == Worker.State.SUCCEEDED) {

                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(0.8), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    public interface InitCompletionHandler {
        void complete();
    }
}
