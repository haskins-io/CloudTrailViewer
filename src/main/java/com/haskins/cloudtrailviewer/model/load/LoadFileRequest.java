/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.model.load;

import com.haskins.cloudtrailviewer.model.filter.Filter;
import java.util.List;

/**
 *
 * @author mark
 */
public class LoadFileRequest {
    
    private final List<String> filenames;
    private Filter filter;
    
    public LoadFileRequest(List<String> files, Filter filter) {
        this.filenames = files;
        this.filter = filter;
    }
    
    public List<String> getFilenames() {
        return this.filenames;
    }
    
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    public Filter getFilter() {
        return this.filter;
    }
}
