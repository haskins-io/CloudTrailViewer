/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.core.EventDatabase;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.ChartFactory;
import com.haskins.cloudtrailviewer.utils.ChartUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark
 */
public class EventsStats extends JPanel implements SideBar, ActionListener {
    
    public static final String NAME = "EventsStats";
    
    private final JMenuBar menu = new JMenuBar();
    
    private final ButtonGroup sourceGroup = new ButtonGroup();
    private final ButtonGroup topGroup = new ButtonGroup();
    private final ButtonGroup styleGroup = new ButtonGroup();
    private final ButtonGroup orientationGroup = new ButtonGroup();
    
    private final JPanel chartCards = new JPanel(new CardLayout());
    
    private final EventDatabase eventDb;
    
    private final DefaultTableModel defaultTableModel = new DefaultTableModel();  
    
    public EventsStats(EventDatabase eventDatabase) {
        
        eventDb = eventDatabase;
        
        buildUI();
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// SideBar implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String getName() {
        return EventsStats.NAME;
    }
    
    @Override
    public void eventLoadingComplete() {
        updateChart();
    }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Pie-Chart-32.png";
    }

    @Override
    public String getTooltip() {
        return "View Events Analysis";
    }

    @Override
    public void setCurrentEvent(Event event) { }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// ActionListener Implementation
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public void actionPerformed(ActionEvent e) {
        updateChart();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
        
        addTable();
        
        addMenu();
        
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.add(menu, BorderLayout.PAGE_START);
        chartPanel.add(chartCards, BorderLayout.CENTER);
        
        this.add(chartPanel, BorderLayout.PAGE_START);
    }
        
    private void addTable() {
        
        defaultTableModel.addColumn("");
        defaultTableModel.addColumn("Property");
        defaultTableModel.addColumn("Value");
        
        final LegendColourRenderer cellRenderer = new LegendColourRenderer();
        JTable table = new JTable(defaultTableModel) {
            
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 0) {
                    return cellRenderer;
                }
                return super.getCellRenderer(row, column);
            }
        };
                
        TableColumn column;
        for (int i = 0; i < 3; i++) {
            column = table.getColumnModel().getColumn(i);
            
            switch(i) {
                case 0:
                    column.setMinWidth(15);
                    column.setMaxWidth(15);
                    column.setPreferredWidth(15);
                    break;

                case 2:
                    column.setMinWidth(40);
                    column.setMaxWidth(40);
                    column.setPreferredWidth(40);
                    break;
            }
        }
        
        JScrollPane tablecrollPane = new JScrollPane(table);
        tablecrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        this.add(tablecrollPane, BorderLayout.CENTER);  
    }
    
    private void addMenu() {
        
        addTopMenu();
        addStyleMenu();
        addOrientationMenu();
        addSourceMenu();
    }
    
    private void addTopMenu() {
        
        JRadioButtonMenuItem mnuTop5 = new JRadioButtonMenuItem("Top 5");
        JRadioButtonMenuItem mnuTop10 = new JRadioButtonMenuItem("Top 10");
        
        mnuTop5.setActionCommand("top.5");
        mnuTop5.addActionListener(this);
        mnuTop5.setSelected(true);
        
        mnuTop10.setActionCommand("top.10");
        mnuTop10.addActionListener(this);
                
        topGroup.add(mnuTop5);
        topGroup.add(mnuTop10);
        
        JMenu menuTop = new JMenu("Top");
        menuTop.add(mnuTop5);
        menuTop.add(mnuTop10);
        
        menu.add(menuTop);  
    }
    
    private void addStyleMenu() {
        
        JRadioButtonMenuItem mnuPie = new JRadioButtonMenuItem("Pie");
        JRadioButtonMenuItem mnuPie3d = new JRadioButtonMenuItem("Pie 3D");
        JRadioButtonMenuItem mnuBar = new JRadioButtonMenuItem("Bar");
        JRadioButtonMenuItem mnuBar3d = new JRadioButtonMenuItem("Bar 3d");
        
        mnuPie.setActionCommand("style.Pie");
        mnuPie.addActionListener(this);
        mnuPie.setSelected(true);
        
        mnuPie3d.setActionCommand("style.Pie3d");
        mnuPie3d.addActionListener(this);
        
        mnuBar.setActionCommand("style.Bar");
        mnuBar.addActionListener(this);
        
        mnuBar3d.setActionCommand("style.Bar3d");
        mnuBar3d.addActionListener(this);
                
        styleGroup.add(mnuPie);
        styleGroup.add(mnuPie3d);
        styleGroup.add(mnuBar);
        styleGroup.add(mnuBar3d);
                
        JMenu menuStyle = new JMenu("Style");
        menuStyle.add(mnuPie);
        menuStyle.add(mnuPie3d);
        menuStyle.add(mnuBar);
        menuStyle.add(mnuBar3d);
        
        menu.add(menuStyle);  
    }
    
    private void addOrientationMenu() {
        
        JRadioButtonMenuItem mnuHorizontal = new JRadioButtonMenuItem("Horizontal");
        JRadioButtonMenuItem mnuVertical = new JRadioButtonMenuItem("Vertical");
        
        mnuHorizontal.setActionCommand("orientation.horizontal");
        mnuHorizontal.addActionListener(this);
        mnuHorizontal.setSelected(true);
        
        mnuVertical.setActionCommand("orientation.vertical");
        mnuVertical.addActionListener(this);
        
        orientationGroup.add(mnuHorizontal);
        orientationGroup.add(mnuVertical);
                
        JMenu menuOrientation = new JMenu("Orientation");
        menuOrientation.add(mnuHorizontal);
        menuOrientation.add(mnuVertical);
        
        menu.add(menuOrientation);  
    }
        
    private void addSourceMenu() {
        
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
         
        sourceGroup.add(mnuEventName);
        sourceGroup.add(mnuAwsRegion);
        sourceGroup.add(mnuSourceIpAddress);
        sourceGroup.add(mnuUserAgent);
        sourceGroup.add(mnuEventSource);
        sourceGroup.add(mnuErrorCode);
        sourceGroup.add(mnuRecipientAccountId);
        
        JMenu sourceMenu = new JMenu("Source");
        sourceMenu.add(mnuEventName);
        sourceMenu.add(mnuAwsRegion);
        sourceMenu.add(mnuSourceIpAddress);
        sourceMenu.add(mnuUserAgent);
        sourceMenu.add(mnuEventSource);
        sourceMenu.add(mnuErrorCode);
        sourceMenu.add(mnuRecipientAccountId);
        sourceMenu.add(getUserIdentityMenu(sourceGroup));
        
        menu.add(sourceMenu); 
    }
    
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
    
    private void updateChart() {
                
        String source = sourceGroup.getSelection().getActionCommand();
                
        String actionCommand = topGroup.getSelection().getActionCommand();
        int periodPos = actionCommand.indexOf(".");
        String topStr = actionCommand.substring(periodPos + 1);
        int top = Integer.parseInt(topStr);
        
        List<Map.Entry<String,Integer>> chartEvents = ChartUtils.getTopEvents(eventDb.getEvents(), top, source);
        
        chartCards.removeAll();
        
        String style = styleGroup.getSelection().getActionCommand();
        
        String orientationCommand = orientationGroup.getSelection().getActionCommand();
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        if (orientationCommand.contains("horizontal")) {
            orientation = PlotOrientation.HORIZONTAL;
        }
        
        ChartPanel cp = ChartFactory.createChart(style, chartEvents, 320, 240, orientation);
        chartCards.add(cp, "");
        chartCards.revalidate();
        
        for (int i = defaultTableModel.getRowCount() -1; i>=0; i--) {
            defaultTableModel.removeRow(i);
        }

        LegendItemCollection legendItems = ((ChartPanel)chartCards.getComponent(0)).getChart().getPlot().getLegendItems();
        
        for (Map.Entry entry : chartEvents) {
            
            Color col = null;
            String key = (String)entry.getKey();
            for (int i=0; i<legendItems.getItemCount(); i++) {
                LegendItem item = legendItems.get(i);
                if (item.getLabel().equalsIgnoreCase(key)) {
                    col = (Color)item.getFillPaint();
                }
            }
            
            defaultTableModel.addRow(new Object[] { col, key, entry.getValue() });
        }
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// Custom TableCellRenderer
    //////////////////////////////////////////////////////////////////////////// 
    class LegendColourRenderer extends JLabel implements TableCellRenderer {

        public LegendColourRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column) {
            
            Color newColor = (Color)color;
            setBackground(newColor);

            return this;
        }
    }
}
