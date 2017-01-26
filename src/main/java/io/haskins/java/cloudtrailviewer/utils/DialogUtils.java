package io.haskins.java.cloudtrailviewer.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Optional;

/**
 * Created by markhaskins on 25/01/2017.
 */
public class DialogUtils {

    public static Optional<String> showTextInputDialog(String title, String message) {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(message);

        return dialog.showAndWait();
    }

    public static Optional<String> showChoiceDialog(String title, String message, List<String> choices) {

        ChoiceDialog<String> dialog = new ChoiceDialog<String>(choices.get(0), choices);
        dialog.setTitle(title);
        dialog.setHeaderText(message);

        return dialog.showAndWait();
    }

    public static void showAlertDialog(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Information Alert");
        alert.setContentText(message);

        alert.show();
    }
}
