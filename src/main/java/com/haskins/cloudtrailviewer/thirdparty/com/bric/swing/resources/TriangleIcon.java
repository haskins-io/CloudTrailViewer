/*
 * @(#)TriangleIcon.java
 *
 * $Date: 2014-04-23 19:38:18 -0500 (Wed, 23 Apr 2014) $
 *
 * Copyright (c) 2011 by Jeremy Wood.
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Jeremy Wood. For details see accompanying license terms.
 * 
 * This software is probably, but not necessarily, discussed here:
 * https://javagraphics.java.net/
 * 
 * That site should also contain the most recent official version
 * of this software.  (See the SVN repository for more details.)
 */
package com.haskins.cloudtrailviewer.thirdparty.com.bric.swing.resources;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;
import javax.swing.SwingConstants;

/** This icon is an isosceles triangle.
 */
public class TriangleIcon implements Icon {
	final protected int height, width, direction;
	protected Color color;
	final protected GeneralPath triangle = new GeneralPath();

	/** Creates a new TriangleIcon.  The fill color used here
	 * is <code>Color.darkGray</code>.
	 * 
	 * @param direction one of the SwingConstant constants for NORTH, SOUTH, EAST or WEST.
	 * For example, if the direction is EAST this triangle will point to the right.
	 * @param width the width of this icon
	 * @param height the height of this icon
	 */
	public TriangleIcon(int direction,int width,int height) {
		this(direction, width, height, Color.darkGray);
	}
	/** Creates a new TriangleIcon.
	 * 
	 * @param direction one of the SwingConstant constants for NORTH, SOUTH, EAST or WEST.
	 * For example, if the direction is EAST this triangle will point to the right.
	 * @param width the width of this icon
	 * @param height the height of this icon
	 * @param color the color to fill this icon with.
	 */
	public TriangleIcon(int direction,int width,int height,Color color) {
            this.direction = direction;
            this.width = width;
            this.height = height;
            this.color = color;

            if(direction==SwingConstants.EAST || direction==SwingConstants.RIGHT) {
                    triangle.moveTo(0,0);
                    triangle.lineTo(width, height/2);
                    triangle.lineTo(0, height);
            } else if(direction==SwingConstants.WEST || direction==SwingConstants.LEFT) {
                    triangle.moveTo(width, 0);
                    triangle.lineTo(0, height/2);
                    triangle.lineTo(width, height);
            } else if(direction==SwingConstants.NORTH || direction==SwingConstants.TOP) {
                    triangle.moveTo(0, height);
                    triangle.lineTo(width/2, 0);
                    triangle.lineTo(width, height);
            } else if(direction==SwingConstants.SOUTH || direction==SwingConstants.BOTTOM) {
                    triangle.moveTo(0, 0);
                    triangle.lineTo(width/2, height);
                    triangle.lineTo(width, 0);
            } else {
                    throw new IllegalArgumentException("direction ("+direction+") must be one of the SwingConstant constants: NORTH, SOUTH, EAST or WEST.");
            }
            triangle.closePath();
	}
	
	public int getDirection() {
            return direction;
	}

	public int getIconHeight() {
            return height;
	}

	public int getIconWidth() {
            return width;
	}
	
	public void setColor(Color c) {
            color = c;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.translate(x, y);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fill(triangle);
	}
}
