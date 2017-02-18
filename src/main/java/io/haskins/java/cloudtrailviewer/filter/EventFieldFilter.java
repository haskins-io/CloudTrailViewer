package io.haskins.java.cloudtrailviewer.filter;

import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;

import javax.swing.*;

/**
 * Created by markhaskins on 18/02/2017.
 */
public class EventFieldFilter extends AbstractFilter {

    private String fieldName;

    private final JLabel filterName = new JLabel();

    public void setOption(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean passesFilter(Event event) {

        boolean passesFilter = false;

        String fieldValue = EventUtils.getEventProperty(fieldName, event);
        if (fieldValue != null) {
            fieldValue = fieldValue.toLowerCase();
            String lowerFilter = this.needle.toLowerCase();
            if (fieldValue.contains(lowerFilter)) {
                passesFilter = true;
            }
        }

        return passesFilter;
    }

    @Override
    public boolean isNeedleSet() {

        boolean needleSet = false;

        if (needle != null && needle.length() > 0) {
            needleSet = true;
        }

        return needleSet;
    }

}

