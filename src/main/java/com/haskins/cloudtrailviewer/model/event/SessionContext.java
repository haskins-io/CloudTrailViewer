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

package com.haskins.cloudtrailviewer.model.event;

import java.io.Serializable;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author mark.haskins
 */
public class SessionContext implements Serializable {

    private static final long serialVersionUID = 6808999959667280297L;

    private Map attributes;
    private SessionIssuer sessionIssuer;
    
    /**
     * @return the attributes
     */
    public Map getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }
    
    /**
     * @return the sessionIssuer
     */
    public SessionIssuer getSessionIssuer() {
        return sessionIssuer;
    }

    /**
     * @param sessionIssuer the sessionIssuer to set
     */
    public void setSessionIssuer(SessionIssuer sessionIssuer) {
        this.sessionIssuer = sessionIssuer;
    }
    
    /**
     * Returns a tree object for the values of the class
     * @return 
     */
    public DefaultMutableTreeNode getTree() {
        
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("Session Context");
        
        if (getSessionIssuer() != null) {
            treeNode.add(getSessionIssuer().getTree());
        }
        
        return treeNode;
    }
    
    @Override
    public String toString() {
        
        StringBuilder modelData = new StringBuilder();
        
        if (getAttributes() != null) { modelData.append(getAttributes().toString()).append(", "); }
        if (getSessionIssuer() != null) { modelData.append(getSessionIssuer().toString()).append(", "); }
        
        return modelData.toString();
    }
}
