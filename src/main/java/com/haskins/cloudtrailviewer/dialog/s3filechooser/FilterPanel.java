/*
 * Copyright (C) 2016 Mark P. Haskins
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

package com.haskins.cloudtrailviewer.dialog.s3filechooser;

import com.haskins.cloudtrailviewer.model.filter.Filter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class FilterPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = -1600052521405754276L;
    
    private final ModePanel modePanel;
    private final FilteringPanel parent;
    private final Filter filter;
    
    public FilterPanel(String filterName, Filter filter, boolean isFirst, FilteringPanel filteringPanel, String mode) {
        
        super(new BorderLayout());
        
        this.filter = filter;
        this.parent = filteringPanel;
        
        modePanel = new ModePanel(mode);
        
        JButton removeFilter = new JButton("Remove");
        removeFilter.addActionListener(this);
        
        if (!isFirst) {
            modePanel.setVisible(true);
            add(modePanel, BorderLayout.PAGE_START);
        }
        
        add(filter.getFilterPanel(filterName), BorderLayout.CENTER);
        add(removeFilter, BorderLayout.LINE_END); 
    }
    
    public void hideAndPanel() {
        modePanel.setVisible(false);
    }
    
    public void setMode(String mode) {
        modePanel.setMode(mode);
    }
    
    public Filter getFilter() {
        return this.filter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.removeFilter(this);
    }
}

class ModePanel extends JPanel {

    private static final long serialVersionUID = -647686474244916429L;

    JLabel modeLabel = new JLabel();
    
    public ModePanel(String mode) {
        
        setMode(mode);
        
        modeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        this.add(modeLabel);
    }
    
    public final void setMode(String mode) {
        modeLabel.setText("-- " + mode + " --");
    }
}
