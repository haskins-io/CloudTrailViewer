package com.haskins.cloudtrailviewer.model;

import java.awt.event.ActionListener;

/**
 * Model class that holds the information needed to show a drop down menu on the Feature toolbar
 *
 * Created by markhaskins on 26/08/2016.
 */
public class FeatureAdditionButton {

    private String icon;
    private String description;
    private ActionListener actionCommand;

    public FeatureAdditionButton(String icon, String desc, ActionListener command) {
        this.icon = icon;
        this.description = desc;
        this.actionCommand = command;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public ActionListener getActionCommand() {
        return actionCommand;
    }
}
