package io.haskins.java.cloudtrailviewer.controller.menu;

import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

/**
 * Controller that handles the File menu
 *
 * Created by markhaskins on 06/01/2017.
 */
@Component
public class FileMenuController {

    @FXML
    public void quit() {
        System.exit(0);
    }
}
