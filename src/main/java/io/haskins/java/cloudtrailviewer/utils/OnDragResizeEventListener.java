package io.haskins.java.cloudtrailviewer.utils;

/**
 * Created by markhaskins on 05/02/2017.
 */
public interface OnDragResizeEventListener {

    void onDrag(double x, double y);
    void onResize(double h, double w);
}