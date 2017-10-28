package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import org.apache.lucene.misc.TermStats;

import java.util.ArrayList;
import java.util.List;

public abstract class DataService {

    abstract List<? extends AwsData> getDataDb();

    public abstract TermStats[] getTop(int top, String series) throws Exception;


    final List<DataServiceListener> listeners = new ArrayList<>();

    public void registerAsListener(DataServiceListener l) {
        listeners.add(l);
    }

    void injectEvents(DataServiceListener l) {
        l.newEvents(getDataDb());
    }

}
