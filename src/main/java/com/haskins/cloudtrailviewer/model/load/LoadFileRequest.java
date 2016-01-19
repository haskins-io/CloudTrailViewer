/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.model.load;

import com.haskins.cloudtrailviewer.model.filter.CompositeFilter;
import java.util.List;

/**
 *
 * @author mark
 */
public class LoadFileRequest {
    
    private final List<String> filenames;
    private CompositeFilter filter;
    
    public LoadFileRequest(List<String> files, CompositeFilter filter) {
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
