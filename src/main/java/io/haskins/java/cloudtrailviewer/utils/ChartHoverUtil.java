package io.haskins.java.cloudtrailviewer.utils;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.util.Collection;
import java.util.function.Function;

/**
 * Adapted from code found here : http://stackoverflow.com/a/38367201
 *
 * Created by markhaskins on 14/02/2017.
 */
public class ChartHoverUtil<T> {

    public static void setupPieChartHovering(Chart chart) {

        if (chart instanceof PieChart) {
            new ChartHoverUtil<PieChart.Data>(
                    data -> String.format("Value = %s", data.getPieValue()),
                    data -> data.getNode())
                    .setupHovering(((PieChart)chart).getData());
        }

        if (chart instanceof XYChart) {
            new ChartHoverUtil<XYChart.Series<String, Number>>(
                    data -> String.format("Value = %s", data),
                    data -> data.getNode())
                    .setupHovering(((XYChart)chart).getData());
        }

    }

    private final Tooltip tooltip = new Tooltip();
    private final SimpleBooleanProperty adjustingTooltip = new SimpleBooleanProperty(false);
    private final Function<T, String> textProvider;
    private final Function<T, Node> nodeProvider;

    private EventHandler<MouseEvent> moveHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (tooltip.isShowing()) {
                setLabelPosition(e);
            }
        }
    };

    private EventHandler<MouseEvent> enterHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            adjustingTooltip.set(true);

            Node chartNode = (Node) e.getSource();

            tooltip.show(chartNode, e.getScreenX(), e.getScreenY());
            setLabelPosition(e);

            ObservableBooleanValue stillHovering = chartNode.hoverProperty().or(adjustingTooltip);
            stillHovering.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean nowHovering) {
                    if (!nowHovering) {
                        stillHovering.removeListener(this);
                        tooltip.hide();
                    }
                }
            });

            T chartData = (T) chartNode.getUserData();
            String txt = textProvider.apply(chartData);
            tooltip.setText(txt);

            adjustingTooltip.set(false);
        }
    };

    private ChartHoverUtil(Function<T, String> textProvider, Function<T, Node> getNode) {
        this.textProvider = textProvider;
        this.nodeProvider = getNode;
        tooltip.addEventFilter(MouseEvent.MOUSE_MOVED, moveHandler);
    }

    private void setupHovering(Collection<T> data) {
        for (T chartData : data) {
            Node node = nodeProvider.apply(chartData);
            if (node != null) {
                node.setUserData(chartData);
                setupNodeHovering(node);
            }
        }
    }

    private void setupNodeHovering(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_MOVED, moveHandler);
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, enterHandler);
    }

    private void setLabelPosition(MouseEvent e) {
        adjustingTooltip.set(true);

        tooltip.setAnchorX(e.getScreenX());
        tooltip.setAnchorY(e.getScreenY() + 20);

        adjustingTooltip.set(false);
    }
}
