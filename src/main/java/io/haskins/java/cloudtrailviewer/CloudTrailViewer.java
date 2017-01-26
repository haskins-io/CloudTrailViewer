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
