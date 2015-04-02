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

import com.haskins.jcloudtrailerviewer.model.ChartData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark
 */
public class TriDataPanelMenu extends JMenuBar implements ActionListener {
    
    private final List<TriDataPanelMenuListener> listeners = new ArrayList<>();
    
    // Top
    JRadioButtonMenuItem mnuTop5 = new JRadioButtonMenuItem("Top 5");
    JRadioButtonMenuItem mnuTop10 = new JRadioButtonMenuItem("Top 10");
    
    // Style
    JRadioButtonMenuItem mnuPie = new JRadioButtonMenuItem("Pie");
    JRadioButtonMenuItem mnuPie3d = new JRadioButtonMenuItem("Pie 3D");
    JRadioButtonMenuItem mnuBar = new JRadioButtonMenuItem("Bar");
    JRadioButtonMenuItem mnuBar3d = new JRadioButtonMenuItem("Bar 3d");
    
    // Top
    JRadioButtonMenuItem mnuHorizontal = new JRadioButtonMenuItem("Horizontal");
    JRadioButtonMenuItem mnuVertical = new JRadioButtonMenuItem("Vertical");
    
    //event
    JRadioButtonMenuItem mnuEventName = new JRadioButtonMenuItem("Event Name");
    JRadioButtonMenuItem mnuAwsRegion = new JRadioButtonMenuItem("AWS Region");
    JRadioButtonMenuItem mnuSourceIpAddress = new JRadioButtonMenuItem("Source IP Address");
    JRadioButtonMenuItem mnuUserAgent = new JRadioButtonMenuItem("User Agent");
    JRadioButtonMenuItem mnuEventSource = new JRadioButtonMenuItem("Event Source");
    JRadioButtonMenuItem mnuErrorCode = new JRadioButtonMenuItem("Error Code");
    JRadioButtonMenuItem mnuRecipientAccountId = new JRadioButtonMenuItem("Recipient Account Id");
    
    // user identity
    JRadioButtonMenuItem mnuUiType = new JRadioButtonMenuItem("Type");
    JRadioButtonMenuItem mnuUiPrincipalId = new JRadioButtonMenuItem("Principal Id");
    JRadioButtonMenuItem mnuUiArn = new JRadioButtonMenuItem("Arn");
    JRadioButtonMenuItem mnuUiAccountId = new JRadioButtonMenuItem("Account Id");
    JRadioButtonMenuItem mnuUiAccessKeyId = new JRadioButtonMenuItem("Access Key Id");
    JRadioButtonMenuItem mnuUiUsername = new JRadioButtonMenuItem("Username");
    JRadioButtonMenuItem mnuUiInvokedBy = new JRadioButtonMenuItem("Invoked By");
    
    // Session Issuer
    JRadioButtonMenuItem mnuSiType = new JRadioButtonMenuItem("Type");
    JRadioButtonMenuItem mnuSiArn = new JRadioButtonMenuItem("Arn");
    JRadioButtonMenuItem mnuSiPrincipalId = new JRadioButtonMenuItem("Principal Id");
    JRadioButtonMenuItem mnuSiAccountId = new JRadioButtonMenuItem("Account Id");
    JRadioButtonMenuItem mnuSiUsername = new JRadioButtonMenuItem("Username");
        
    public TriDataPanelMenu() {
        
        this.add(getTopMenu());
        this.add(getStyleMenu());
        this.add(getOrientationMenu());
        this.add(getSourceMenu());
    }
    
    public void addListener(TriDataPanelMenuListener l) {
        listeners.add(l);
    }
    
    public void setTop(int top) {
        
        if (top == 5) {
            mnuTop5.setSelected(true);
        } else if (top == 10) {
            mnuTop10.setSelected(true);
        }
    }
    
    public void setStyle(String style) {
        
        if (style.equalsIgnoreCase("Pie")) {
            mnuPie.setSelected(true);
        } else if (style.equalsIgnoreCase("Bar")) {
            mnuBar.setSelected(true);
        }
    }
    
    public void setOrientation(PlotOrientation orientation) {
        
        if (orientation == ChartData.HORIZONTAL) {
            mnuHorizontal.setSelected(true);
        } else if (orientation == ChartData.VERTICAL) {
            mnuVertical.setSelected(true);
        }
    }
    
    public void setSource(String source) {
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        handleTopActions(actionCommand);
        handleStyleActions(actionCommand);
        handleSOrientationActions(actionCommand);
        handleSourceActions(actionCommand);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // private
    ////////////////////////////////////////////////////////////////////////////
    private JMenu getTopMenu() {
        
        mnuTop5.setActionCommand("5");
        mnuTop5.addActionListener(this);
        
        mnuTop10.setActionCommand("10");
        mnuTop10.addActionListener(this);
                
        ButtonGroup topGroup = new ButtonGroup();
        topGroup.add(mnuTop5);
        topGroup.add(mnuTop10);
        
        JMenu menuTop = new JMenu("Top");
        menuTop.add(mnuTop5);
        menuTop.add(mnuTop10);
        
        return menuTop;
    }
    
    private JMenu getStyleMenu() {
        
        mnuPie.setActionCommand("style.Pie");
        mnuPie.addActionListener(this);
        
        mnuPie3d.setActionCommand("style.Pie3d");
        mnuPie3d.addActionListener(this);
        
        mnuBar.setActionCommand("style.Bar");
        mnuBar.addActionListener(this);
        
        mnuBar3d.setActionCommand("style.Bar3d");
        mnuBar3d.addActionListener(this);
        
        ButtonGroup styleGroup = new ButtonGroup();
        styleGroup.add(mnuPie);
        styleGroup.add(mnuPie3d);
        styleGroup.add(mnuBar);
        styleGroup.add(mnuBar3d);
                
        JMenu menuStyle = new JMenu("Style");
        menuStyle.add(mnuPie);
        menuStyle.add(mnuPie3d);
        menuStyle.add(mnuBar);
        menuStyle.add(mnuBar3d);
        
        return menuStyle;
    }
    
    private JMenu getOrientationMenu() {
        
        mnuHorizontal.setActionCommand("orientation.horizontal");
        mnuHorizontal.addActionListener(this);
        
        mnuVertical.setActionCommand("orientation.vertical");
        mnuVertical.addActionListener(this);
        
        ButtonGroup styleGroup = new ButtonGroup();
        styleGroup.add(mnuHorizontal);
        styleGroup.add(mnuVertical);
                
        JMenu menu = new JMenu("Orientation");
        menu.add(mnuHorizontal);
        menu.add(mnuVertical);
        
        return menu;
    }
    
    private JMenu getSourceMenu() {
        
        mnuEventName.setActionCommand("EventName");
        mnuEventName.addActionListener(this);
        
        mnuAwsRegion.setActionCommand("AwsRegion");
        mnuAwsRegion.addActionListener(this);
        
        mnuSourceIpAddress.setActionCommand("SourceIPAddress");
        mnuSourceIpAddress.addActionListener(this);
        
        mnuUserAgent.setActionCommand("UserAgent");
        mnuUserAgent.addActionListener(this);
        
        mnuEventSource.setActionCommand("EventSource");
        mnuEventSource.addActionListener(this);
        
        mnuErrorCode.setActionCommand("ErrorCode");
        mnuErrorCode.addActionListener(this);
        
        mnuRecipientAccountId.setActionCommand("RecipientAccountId");
        mnuRecipientAccountId.addActionListener(this);
         
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(mnuEventName);
        buttonGroup.add(mnuAwsRegion);
        buttonGroup.add(mnuSourceIpAddress);
        buttonGroup.add(mnuUserAgent);
        buttonGroup.add(mnuEventSource);
        buttonGroup.add(mnuErrorCode);
        buttonGroup.add(mnuRecipientAccountId);
        
        JMenu menu = new JMenu("Source");
        menu.add(mnuEventName);
        menu.add(mnuAwsRegion);
        menu.add(mnuSourceIpAddress);
        menu.add(mnuUserAgent);
        menu.add(mnuEventSource);
        menu.add(mnuErrorCode);
        menu.add(mnuRecipientAccountId);
        
        menu.add(getUserIdentityMenu(buttonGroup));
        
        return menu;
    }
    
    private JMenu getUserIdentityMenu(ButtonGroup buttonGroup) {
        
        mnuUiType.setActionCommand("UserIdentity.Type");
        mnuUiType.addActionListener(this);
        
        mnuUiPrincipalId.setActionCommand("UserIdentity.PrincipalId");
        mnuUiPrincipalId.addActionListener(this);
        
        mnuUiArn.setActionCommand("UserIdentity.Arn");
        mnuUiArn.addActionListener(this);
        
        mnuUiAccountId.setActionCommand("UserIdentity.AccountId");
        mnuUiAccountId.addActionListener(this);
        
        mnuUiAccessKeyId.setActionCommand("UserIdentity.AccessKeyId");
        mnuUiAccessKeyId.addActionListener(this);
        
        mnuUiUsername.setActionCommand("UserIdentity.UserName");
        mnuUiUsername.addActionListener(this);
        
        mnuUiInvokedBy.setActionCommand("UserIdentity.InvokedBy");
        mnuUiInvokedBy.addActionListener(this);
         
        buttonGroup.add(mnuUiType);
        buttonGroup.add(mnuUiPrincipalId);
        buttonGroup.add(mnuUiArn);
        buttonGroup.add(mnuUiAccountId);
        buttonGroup.add(mnuUiAccessKeyId);
        buttonGroup.add(mnuUiUsername);
        buttonGroup.add(mnuUiInvokedBy);
        
        JMenu menu = new JMenu("User Identity");
        menu.add(mnuUiType);
        menu.add(mnuUiPrincipalId);
        menu.add(mnuUiArn);
        menu.add(mnuUiAccountId);
        menu.add(mnuUiAccessKeyId);
        menu.add(mnuUiUsername);
        menu.add(mnuUiInvokedBy);
        
        menu.add(getSessionContextMenu(buttonGroup));
        
        return menu;
    }
    
    private JMenu getSessionContextMenu(ButtonGroup buttonGroup) {
        
        JMenu menu = new JMenu("Session Context");
        menu.add(getSessionIssuerMenu(buttonGroup));
        
        return menu;
    }
    
    private JMenu getSessionIssuerMenu(ButtonGroup buttonGroup) {
                
        mnuSiType.setActionCommand("SessionContext.SessionIssuer.UserIdentity.Type");
        mnuSiType.addActionListener(this);
        
        mnuSiPrincipalId.setActionCommand("SessionContext.SessionIssuer.UserIdentity.PrincipalId");
        mnuSiPrincipalId.addActionListener(this);
        
        mnuSiArn.setActionCommand("SessionContext.SessionIssuer.UserIdentity.Arn");
        mnuSiArn.addActionListener(this);
        
        mnuSiAccountId.setActionCommand("SessionContext.SessionIssuer.UserIdentity.AccountId");
        mnuSiAccountId.addActionListener(this);
                
        mnuSiUsername.setActionCommand("SessionContext.SessionIssuer.UserIdentity.UserName");
        mnuSiUsername.addActionListener(this);
        
         
        buttonGroup.add(mnuSiType);
        buttonGroup.add(mnuSiPrincipalId);
        buttonGroup.add(mnuSiArn);
        buttonGroup.add(mnuSiAccountId);
        buttonGroup.add(mnuSiUsername);
        
        JMenu menu = new JMenu("Session Issuer");
        menu.add(mnuSiType);
        menu.add(mnuSiPrincipalId);
        menu.add(mnuSiArn);
        menu.add(mnuSiAccountId);
        menu.add(mnuSiUsername);
        
        return menu;
    }
    
    private void handleTopActions(String actionCommand) {
        
        switch(actionCommand) {
            case "5":
            case "10":
                fireTopUpdated(Integer.parseInt(actionCommand));
                break;
        }
    }
    
    private void handleStyleActions(String actionCommand) {
        
        switch(actionCommand) {
            case "style.Pie":
            case "style.Pie3d":
            case "style.Bar":
            case "style.Bar3d":
                
                int periodPos = actionCommand.indexOf(".");
                String style = actionCommand.substring(periodPos);
                fireStyleUpdated(style);
                break;
        }
    }
    
    private void handleSOrientationActions(String actionCommand) {
        
        switch(actionCommand) {
            case "orientation.horizontal":
                fireOrientationUpdated(PlotOrientation.HORIZONTAL);
                break;
            case "orientation.vertical":
                fireOrientationUpdated(PlotOrientation.VERTICAL);
                break;
        }
    }
    
    private void handleSourceActions(String actionCommand) {
        
        switch(actionCommand) {
            case "EventName":
            case "EventSource":
            case "SourceIPAddress":
            case "UserAgent":
            case "AwsRegion":
            case "ErrorCode":
            case "RecipientAccountId":
            case "UserIdentity.Type":
            case "UserIdentity.PrincipalId":
            case "UserIdentity.Arn":
            case "UserIdentity.UserName":
            case "UserIdentity.AccountId":
            case "UserIdentity.InvokedBy":
            case "UserIdentity.AccessKeyId":
            case "UserIdentity.SessionContext.SessionIssuer.Type":
            case "UserIdentity.SessionContext.SessionIssuer.PrincipalId":
            case "UserIdentity.SessionContext.SessionIssuer.Arn":
            case "UserIdentity.SessionContext.SessionIssuer.UserName":
            case "UserIdentity.SessionContext.SessionIssuer.AccountId":
                fireSourceUpdated(actionCommand);
                break;
        }
    }
    
    private void fireTopUpdated(int newTop) {
        
        for (TriDataPanelMenuListener l : listeners) {
            l.topUpdated(newTop);
        }
    }

    private void fireStyleUpdated(String newStyle) {
        
        for (TriDataPanelMenuListener l : listeners) {
            l.styleUpdated(newStyle);
        }
    }
    
    private void fireOrientationUpdated(PlotOrientation newOrientation) {
        
        for (TriDataPanelMenuListener l : listeners) {
            l.orientationUpdated(newOrientation);
        }
    }
    
    private void fireSourceUpdated(String newSource) {
        
        for (TriDataPanelMenuListener l : listeners) {
            l.sourceUpdated(newSource);
        }
    }
}
