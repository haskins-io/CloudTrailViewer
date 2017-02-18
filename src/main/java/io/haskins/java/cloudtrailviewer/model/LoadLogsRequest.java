package io.haskins.java.cloudtrailviewer.model;

import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;

import java.util.List;

/**
 * Created by markhaskins on 18/02/2017.
 */
public class LoadLogsRequest {

    private final List<String> filenames;
    private CompositeFilter filter;

    public LoadLogsRequest(List<String> files, CompositeFilter filter) {
        this.filenames = files;
        this.filter = filter;
    }

    public List<String> getFilenames() {
        return this.filenames;
    }

    public void setFilter(CompositeFilter filter) {
        this.filter = filter;
    }
    public CompositeFilter getFilter() {
        return this.filter;
    }
}
