package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;

import java.util.ArrayList;
import java.util.List;

public abstract class DataService {

    abstract List<? extends AwsData> getDataDb();

    final List<DataServiceListener> listeners = new ArrayList<>();

    public void registerAsListener(DataServiceListener l) {
        listeners.add(l);
    }

    void injectEvents(DataServiceListener l) {
        l.newEvents(getDataDb());
    }

}
