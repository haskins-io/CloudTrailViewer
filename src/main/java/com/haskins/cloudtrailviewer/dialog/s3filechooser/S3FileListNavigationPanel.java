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

import com.haskins.cloudtrailviewer.thirdparty.com.bric.plaf.BreadCrumbUI;
import com.haskins.cloudtrailviewer.thirdparty.com.bric.swing.JBreadCrumb;
import com.haskins.cloudtrailviewer.thirdparty.com.bric.swing.NavigationListener;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.LabelUI;

/**
 *
 * @author markhaskins
 */
public class S3FileListNavigationPanel extends JPanel {

    private static final long serialVersionUID = -7245639134496141173L;
    
    private final JBreadCrumb<String> locationCrumb = new JBreadCrumb<>();
        
    public S3FileListNavigationPanel(NavigationListener l) {
        
        this.setLayout(new BorderLayout());
        
        addHomeButton();
        createBreadCrumb(l);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // public methods
    ////////////////////////////////////////////////////////////////////////////
    public void setBreadCrumb(String[] path) {
        
        locationCrumb.setPath(path);
    }
    
    public String[] getBreadCrumbPath() {
        return locationCrumb.getPath();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private void addHomeButton() {
        
        JButton btnHome = new JButton();
        ToolBarUtils.addImageToButton(btnHome, "Home-32.png", "Home", "Home");
        btnHome.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
//                prefix = "";
//                reloadContents();
            }
        });

        this.add(btnHome, BorderLayout.LINE_START);
    }
    
    private void createBreadCrumb(NavigationListener l) {

        Icon lankySeparator = new Icon() {

            int separatorWidth = 5;
            int leftPadding = 3;
            int rightPadding = 5;

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int h = getIconHeight() - 1;
                GeneralPath arrow = new GeneralPath();
                arrow.moveTo(x + leftPadding, y);
                arrow.lineTo(x + leftPadding + separatorWidth, y + h / 2);
                arrow.lineTo(x + leftPadding, y + h);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(new Color(0, 0, 0, 10));
                g2.draw(arrow);
                g2.setStroke(new BasicStroke(1));
                g2.setColor(new Color(0, 0, 0, 40));
                g2.draw(arrow);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return separatorWidth + leftPadding + rightPadding;
            }

            @Override
            public int getIconHeight() {
                return 22;
            }

        };

        locationCrumb.setUI(new BreadCrumbUI() {

            @Override
            protected LabelUI getLabelUI() {
                return null;
            }
        });

        locationCrumb.setFormatter(new JBreadCrumb.BreadCrumbFormatter<String>() {

            @Override
            public void format(JBreadCrumb<String> container, JLabel label, String pathNode, int index) {
                label.setText(pathNode);
            }

        });

        locationCrumb.putClientProperty(BreadCrumbUI.SEPARATOR_ICON_KEY, lankySeparator);
        locationCrumb.setBorder(new EmptyBorder(0, 5, 0, 0));
        locationCrumb.setOpaque(true);

        Dimension d = locationCrumb.getPreferredSize();
        d.width -= 100;
        locationCrumb.setPreferredSize(d);

        locationCrumb.addNavigationListener(l);
        
        this.add(locationCrumb, BorderLayout.CENTER);
    }
}
