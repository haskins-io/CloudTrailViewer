package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

/**
 * Created by markhaskins on 27/01/2017.
 */
public interface FileListControllerListener {

    /**
     * Fired when a selection is made
     *
     * @param isValid
     */
    void listItemSelected(boolean isValid);

    /**
     * Fired when files / folder has been selected and the dialog can be closed.
     */
    void selectionComplete();

    /**
     * Fired when an exception is caught
     *
     * @param e
     */
    void exceptionCaught(Exception e);

}
