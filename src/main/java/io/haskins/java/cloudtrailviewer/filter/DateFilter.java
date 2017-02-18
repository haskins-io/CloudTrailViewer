package io.haskins.java.cloudtrailviewer.filter;

import io.haskins.java.cloudtrailviewer.model.event.Event;
import org.joda.time.DateTime;


/**
 * Created by markhaskins on 18/02/2017.
 */
public class DateFilter extends AbstractFilter {

    private boolean fromDate = true;

    private DateTime selectedDateTime;

    public boolean passesFilter(Event event) {

        boolean passes = true;

//        if (fromDate) {
//            passes = afterFromDate(event);
//        } else {
//            passes = beforeToDate(event);
//        }

        return passes;
    }

    @Override
    public boolean isNeedleSet() {

        boolean needleSet = false;

        if (selectedDateTime != null) {
            needleSet = true;
        }

        return needleSet;
    }

}
