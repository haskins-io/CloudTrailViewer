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

package io.haskins.java.cloudtrailviewer.utils;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * This class enables a widget to be dragged around, as well as providing a resize functionality.
 *
 * This class is based on work done by varren on GitHub : https://github.com/varren/JavaFX-Resizable-Draggable-Node
 *
 * Created by markhaskins on 23/01/2017.
 */
public class DragResizeWidget {

    public interface OnDragResizeEventListener {

        void onDrag(double x, double y);
        void onResize(double h, double w);
    }

    public enum S {
        DEFAULT,
        DRAG,
        SE_RESIZE
    }

    private double clickX, clickY, nodeX, nodeY, nodeH, nodeW;

    private S state = S.DEFAULT;

    private final Node node;

    private final OnDragResizeEventListener listener;

    private static final int MARGIN = 8;

    private DragResizeWidget(Node theNode, OnDragResizeEventListener listener) {

        this.node = theNode;
        this.listener = listener;
    }

    public static void makeResizable(Node theNode, OnDragResizeEventListener listener) {

        final DragResizeWidget resizer = new DragResizeWidget(theNode, listener);

        theNode.setOnMousePressed(resizer::mousePressed);
        theNode.setOnMouseDragged(resizer::mouseDragged);
        theNode.setOnMouseMoved(resizer::mouseOver);
        theNode.setOnMouseReleased(resizer::mouseReleased);
    }

    private void mouseReleased(MouseEvent event) {

        node.setCursor(Cursor.DEFAULT);
        state = S.DEFAULT;
    }

    private void mouseOver(MouseEvent event) {

        S state = currentMouseState(event);
        Cursor cursor = getCursorForState(state);
        node.setCursor(cursor);
    }

    private S currentMouseState(MouseEvent event) {

        S state = S.DEFAULT;

        boolean right = isRightResizeZone(event);
        boolean bottom = isBottomResizeZone(event);

        if (right && bottom) {
            state = S.SE_RESIZE;
        }
        else if (isInDragZone(event)) {
            state = S.DRAG;
        }

        return state;
    }

    private static Cursor getCursorForState(S state) {

        switch (state) {
            case SE_RESIZE:
                return Cursor.SE_RESIZE;
            case DRAG:
                return Cursor.MOVE;
            default:
                return Cursor.DEFAULT;
        }
    }

    private void mouseDragged(MouseEvent event) {


        double mouseX = parentX(event.getX());
        double mouseY = parentY(event.getY());

        if (state == S.DRAG) {
            listener.onDrag(mouseX - clickX, mouseY - clickY);
        } else  if (state != S.DEFAULT) {
            double newH = nodeH;
            double newW = nodeW;

            if (state == S.SE_RESIZE) {
                newW = mouseX - nodeX;
                newH = mouseY - nodeY;
            }

            listener.onResize(newH, newW);
        }
    }

    private void mousePressed(MouseEvent event) {

        node.toFront();

        if (isInResizeZone(event)) {

            setNewInitialEventCoordinates(event);
            state = currentMouseState(event);

        } else if (isInDragZone(event)) {

            clickX = event.getX();
            clickY = event.getY();

            state = S.DRAG;

        } else {
            state = S.DEFAULT;
        }
    }

    private void setNewInitialEventCoordinates(MouseEvent event) {

        nodeX = nodeX();
        nodeY = nodeY();
        nodeH = nodeH();
        nodeW = nodeW();
        clickX = event.getX();
        clickY = event.getY();
    }

    private boolean isInResizeZone(MouseEvent event) {

        return isRightResizeZone(event) || isBottomResizeZone(event) ;
    }

    private boolean isInDragZone(MouseEvent event) {

        double xPos = parentX(event.getX());
        double yPos = parentY(event.getY());
        double nodeX = nodeX() + 1;
        double nodeY = nodeY() + 1;
        double nodeX0 = nodeX() + nodeW() - 1;
        double nodeY0 = nodeY() + nodeH() - 1;

        return (xPos > nodeX && xPos < nodeX0) && (yPos > nodeY && yPos < nodeY0);
    }

    private boolean isRightResizeZone(MouseEvent event) {
        return intersect(this.node.getBoundsInParent().getWidth(), event.getX());
    }

    private boolean isBottomResizeZone(MouseEvent event) {
        return intersect(this.node.getBoundsInParent().getHeight(), event.getY());
    }

    private boolean intersect(double side, double point) {
        return side + MARGIN > point && side - MARGIN < point;
    }

    private double parentX(double localX) {
        return nodeX() + localX;
    }

    private double parentY(double localY) {
        return nodeY() + localY;
    }

    private double nodeX() {
        return this.node.getBoundsInParent().getMinX();
    }

    private double nodeY() {
        return this.node.getBoundsInParent().getMinY();
    }

    private double nodeW() {
        return this.node.getBoundsInParent().getWidth();
    }

    private double nodeH() {
        return this.node.getBoundsInParent().getHeight();
    }

}
