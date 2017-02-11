package io.haskins.java.cloudtrailviewer.controller.components;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

/**
 * Created by markhaskins on 11/02/2017.
 */
public class UserGuideController {

    @FXML
    WebView webView;

    public void setHTML(String HTML) {
        webView.getEngine().loadContent(HTML);
    }
}
