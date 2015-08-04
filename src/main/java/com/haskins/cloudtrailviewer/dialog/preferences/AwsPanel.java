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

package com.haskins.cloudtrailviewer.dialog.preferences;

import com.haskins.cloudtrailviewer.dialog.preferences.components.AwsAccountPanel;
import com.haskins.cloudtrailviewer.dialog.preferences.components.AwsAliasPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class AwsPanel extends JPanel implements Preferences {
    
    private final AwsAccountPanel accounts = new AwsAccountPanel();
    private final AwsAliasPanel aliases = new AwsAliasPanel();
    
    public AwsPanel() {
        buildUI();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Preferences implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void savePreferences() {
        
        accounts.savePreferences();
        aliases.savePreferences();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        JPanel container = new JPanel(new GridLayout(2,1));
        
        container.add(accounts);
        container.add(aliases);
        
        this.setLayout(new BorderLayout());
        this.add(container, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
