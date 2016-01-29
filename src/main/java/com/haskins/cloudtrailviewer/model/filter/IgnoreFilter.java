/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.haskins.cloudtrailviewer.model.filter;

import com.haskins.cloudtrailviewer.dao.DbManager;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class IgnoreFilter extends AbstractFilter implements Serializable {
    
    private static final long serialVersionUID = -2890390951885621912L;
    
    private final List<String> ignores = new ArrayList<>();
    
    public IgnoreFilter() {
        
        String query = "SELECT ignore FROM ctv_ignores";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            String ignore = ((String)row.get("ignore")).toLowerCase();
            ignores.add(ignore.replaceAll("\\*", ".*"));
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Filter overrides
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        String eventName = event.getEventName().toLowerCase();
        for (String ignore : ignores) {
            passesFilter |= eventName.matches(ignore);
        }
        
        return !passesFilter;
    }
    
    @Override
    public JPanel getFilterPanel(String name) {
        
        JPanel ui = new JPanel();
        ui.add(new JLabel("Exclude Ignore Actions"));
        
        ui.setMinimumSize(DEFAULT_SIZE);
        ui.setPreferredSize(DEFAULT_SIZE);
        ui.setMaximumSize(DEFAULT_SIZE);
        
        return ui;
    }
    
    @Override
    public boolean isNeedleSet() {
        return true;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Other overrides
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return "Ingore";
    }
}
