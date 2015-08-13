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
package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.core.EventDatabase;
import com.haskins.cloudtrailviewer.utils.ChartUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author mark.haskins
 */
public class EventsChart extends AbstractChart implements ActionListener {

    public EventsChart(EventDatabase eventDatabase, EventTablePanel eventTable) {

        super(eventDatabase, eventTable);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract methods implementation
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public void addCustomMenu() {

        JRadioButtonMenuItem mnuEventName = new JRadioButtonMenuItem("Event Name");
        JRadioButtonMenuItem mnuAwsRegion = new JRadioButtonMenuItem("AWS Region");
        JRadioButtonMenuItem mnuSourceIpAddress = new JRadioButtonMenuItem("Source IP Address");
        JRadioButtonMenuItem mnuUserAgent = new JRadioButtonMenuItem("User Agent");
        JRadioButtonMenuItem mnuEventSource = new JRadioButtonMenuItem("Event Source");
        JRadioButtonMenuItem mnuErrorCode = new JRadioButtonMenuItem("Error Code");
        JRadioButtonMenuItem mnuRecipientAccountId = new JRadioButtonMenuItem("Recipient Account Id");

        mnuEventName.setActionCommand("EventName");
        mnuEventName.addActionListener(this);
        mnuEventName.setSelected(true);

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

        customGroup.add(mnuEventName);
        customGroup.add(mnuAwsRegion);
        customGroup.add(mnuSourceIpAddress);
        customGroup.add(mnuUserAgent);
        customGroup.add(mnuEventSource);
        customGroup.add(mnuErrorCode);
        customGroup.add(mnuRecipientAccountId);

        JMenu sourceMenu = new JMenu("Event");
        sourceMenu.add(mnuEventName);
        sourceMenu.add(mnuAwsRegion);
        sourceMenu.add(mnuSourceIpAddress);
        sourceMenu.add(mnuUserAgent);
        sourceMenu.add(mnuEventSource);
        sourceMenu.add(mnuErrorCode);
        sourceMenu.add(mnuRecipientAccountId);
        sourceMenu.add(getUserIdentityMenu(customGroup));

        menu.add(sourceMenu);
    }

    @Override
    public void update() {

        String source = customGroup.getSelection().getActionCommand();

        String actionCommand = topGroup.getSelection().getActionCommand();
        int periodPos = actionCommand.indexOf(".");
        String topStr = actionCommand.substring(periodPos + 1);
        int top = Integer.parseInt(topStr);

        List<Map.Entry<String, Integer>> chartEvents = ChartUtils.getTopEvents(eventDb.getEvents(), top, source);
        updateChart(chartEvents);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// SideBar implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
        update();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// ActionListener Implementation
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private JMenu getUserIdentityMenu(ButtonGroup buttonGroup) {

        JRadioButtonMenuItem mnuUiType = new JRadioButtonMenuItem("Type");
        JRadioButtonMenuItem mnuUiPrincipalId = new JRadioButtonMenuItem("Principal Id");
        JRadioButtonMenuItem mnuUiArn = new JRadioButtonMenuItem("Arn");
        JRadioButtonMenuItem mnuUiAccountId = new JRadioButtonMenuItem("Account Id");
        JRadioButtonMenuItem mnuUiAccessKeyId = new JRadioButtonMenuItem("Access Key Id");
        JRadioButtonMenuItem mnuUiUsername = new JRadioButtonMenuItem("Username");
        JRadioButtonMenuItem mnuUiInvokedBy = new JRadioButtonMenuItem("Invoked By");

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

        JMenu userIdentityMenu = new JMenu("User Identity");
        userIdentityMenu.add(mnuUiType);
        userIdentityMenu.add(mnuUiPrincipalId);
        userIdentityMenu.add(mnuUiArn);
        userIdentityMenu.add(mnuUiAccountId);
        userIdentityMenu.add(mnuUiAccessKeyId);
        userIdentityMenu.add(mnuUiUsername);
        userIdentityMenu.add(mnuUiInvokedBy);
        userIdentityMenu.add(getSessionContextMenu(buttonGroup));

        return userIdentityMenu;
    }

    private JMenu getSessionContextMenu(ButtonGroup buttonGroup) {

        JMenu sessionContext = new JMenu("Session Context");
        sessionContext.add(getSessionIssuerMenu(buttonGroup));

        return sessionContext;
    }

    private JMenu getSessionIssuerMenu(ButtonGroup buttonGroup) {

        JRadioButtonMenuItem mnuSiType = new JRadioButtonMenuItem("Type");
        JRadioButtonMenuItem mnuSiArn = new JRadioButtonMenuItem("Arn");
        JRadioButtonMenuItem mnuSiPrincipalId = new JRadioButtonMenuItem("Principal Id");
        JRadioButtonMenuItem mnuSiAccountId = new JRadioButtonMenuItem("Account Id");
        JRadioButtonMenuItem mnuSiUsername = new JRadioButtonMenuItem("Username");

        mnuSiType.setActionCommand("UserIdentity.SessionContext.SessionIssuer.Type");
        mnuSiType.addActionListener(this);

        mnuSiPrincipalId.setActionCommand("UserIdentity.SessionContext.SessionIssuer.PrincipalId");
        mnuSiPrincipalId.addActionListener(this);

        mnuSiArn.setActionCommand("UserIdentity.SessionContext.SessionIssuer.Arn");
        mnuSiArn.addActionListener(this);

        mnuSiAccountId.setActionCommand("UserIdentity.SessionContext.SessionIssuer.AccountId");
        mnuSiAccountId.addActionListener(this);

        mnuSiUsername.setActionCommand("UserIdentity.SessionContext.SessionIssuer.UserName");
        mnuSiUsername.addActionListener(this);

        buttonGroup.add(mnuSiType);
        buttonGroup.add(mnuSiPrincipalId);
        buttonGroup.add(mnuSiArn);
        buttonGroup.add(mnuSiAccountId);
        buttonGroup.add(mnuSiUsername);

        JMenu siMenu = new JMenu("Session Issuer");
        siMenu.add(mnuSiType);
        siMenu.add(mnuSiPrincipalId);
        siMenu.add(mnuSiArn);
        siMenu.add(mnuSiAccountId);
        siMenu.add(mnuSiUsername);

        return siMenu;
    }
}
