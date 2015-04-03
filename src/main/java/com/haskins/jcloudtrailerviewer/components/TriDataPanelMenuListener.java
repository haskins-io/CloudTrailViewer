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
package com.haskins.jcloudtrailerviewer.components;

import org.jfree.chart.plot.PlotOrientation;

/**
 * Interface that models a Listener.
 * 
 * The methods on this interface are called when changes are made to Menu provided
 * by {@link com.haskins.jcloudtrailerviewer.components.TriDataPanelMenu}
 * 
 * @author mark
 */
public interface TriDataPanelMenuListener {
    
    /**
     * called when the Top value is updated.
     * @param newTop new Top value
     */
    public void topUpdated(int newTop);
    
    /**
     * called when the style of the chart is updated.
     * @param newStyle new style of the chart e.g. Pie
     */
    public void styleUpdated(String newStyle);
    
    /**
     * Called when the chart orientation is changed.
     * @param newOrientation new Orientation e.g. HORIZONTAL
     */
    public void orientationUpdated(PlotOrientation newOrientation);
    
    /**
     * called when the source of the chart is changed.
     * @param newSource new source e.g. EventName or UserIdentity.UserName
     */
    public void sourceUpdated(String newSource);
}
