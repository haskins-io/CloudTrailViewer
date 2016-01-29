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

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.core.EventDatabase;
import com.haskins.cloudtrailviewer.dao.AccountDao;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailDialog;
import com.haskins.cloudtrailviewer.dialog.resourcedetail.ResourceDetailRequest;
import com.haskins.cloudtrailviewer.model.AwsAccount;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.model.filter.AllFilter;
import com.haskins.cloudtrailviewer.utils.ChartFactory;
import com.haskins.cloudtrailviewer.utils.ChartUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Abstract class that provides common functionality for the Sidebar chart
 *  
 * @author mark.haskins
 */
public abstract class AbstractChart extends JPanel implements SideBar, ActionListener {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    protected final ChartUtils chartUtils = new ChartUtils();
    
    public static final String NAME = "Chart";
    private static final long serialVersionUID = 7624167880255656811L;

    protected final JMenuBar menu = new JMenuBar();

    protected final ButtonGroup customGroup = new ButtonGroup();
    protected final ButtonGroup topGroup = new ButtonGroup();
    protected final ButtonGroup styleGroup = new ButtonGroup();
    protected final ButtonGroup orientationGroup = new ButtonGroup();

    protected final JPanel chartCards = new JPanel(new CardLayout());

    protected final EventTablePanel eventTablePanel;

    protected final DefaultTableModel defaultTableModel = new DefaultTableModel();
    
    protected final EventDatabase eventDb;

    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract method declarations
    ////////////////////////////////////////////////////////////////////////////
    public abstract void update();
    public abstract void addCustomMenu();
    
    public AbstractChart(EventDatabase eventDatabase, EventTablePanel eventTable) {

        eventDb = eventDatabase;
        eventTablePanel = eventTable;

        buildUI();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// SideBar implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String getName() {
        return AbstractChart.NAME;
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
    public void setCurrentEvent(Event event) {
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// ActionListener Implementation
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// protected methods
    //////////////////////////////////////////////////////////////////////////// 
    /**
     * Returns the value currently selectd in the Top menu
     * @return 
     */
    protected int getTopXValue() {
        
        String actionCommand = topGroup.getSelection().getActionCommand();
        int periodPos = actionCommand.indexOf('.');
        String topStr = actionCommand.substring(periodPos + 1);
        
        return Integer.parseInt(topStr);
    }
    
    /**
     * updates the chart, changing the menus if needed and updating the contents
     * of the Key table.
     * @param chartData 
     */
    protected void updateChart(List<Map.Entry<String, Integer>> chartData) {
        
        chartCards.removeAll();
        
        for (int i = defaultTableModel.getRowCount() - 1; i >= 0; i--) {
            defaultTableModel.removeRow(i);
        }
        
        if (chartData != null && chartData.size() > 0) {
            
            String style = styleGroup.getSelection().getActionCommand();

            String orientationCommand = orientationGroup.getSelection().getActionCommand();
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            if (orientationCommand.contains("horizontal")) {
                orientation = PlotOrientation.HORIZONTAL;
            }

            ChartPanel cp = ChartFactory.createChart(style, chartData, 320, 240, orientation);
            chartCards.add(cp, "");
            chartCards.revalidate();

            LegendItemCollection legendItems = ((ChartPanel) chartCards.getComponent(0)).getChart().getPlot().getLegendItems();

            for (Map.Entry entry : chartData) {

                Color col = null;
                String key = (String) entry.getKey();
                for (int i = 0; i < legendItems.getItemCount(); i++) {
                    LegendItem item = legendItems.get(i);
                    if (item.getLabel().equalsIgnoreCase(key)) {
                        col = (Color) item.getFillPaint();
                    }
                }

                if (key.contains(":i-")) {
                    
                    int posColon = key.indexOf(':');
                    key = key.substring(posColon + 1);
                }
                
                defaultTableModel.addRow(new Object[]{col, key, entry.getValue()});
            }
        }
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
        final JTable table = new JTable(defaultTableModel) {
            
            private static final long serialVersionUID = -6272711583089149891L;

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 0) {
                    return cellRenderer;
                }
                return super.getCellRenderer(row, column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent me) {
                
                JTable table =(JTable) me.getSource();
                String value = (String) defaultTableModel.getValueAt(table.getSelectedRow(), 1);
                
                if (me.getClickCount() == 2) {
                    
                    if (value.startsWith("i-")) {
                        
                        Event event = null;
                        AllFilter filter = new AllFilter();
                        filter.setNeedle(value);
                        for (Event searchEvent : eventDb.getEvents()) {
                            
                            if (filter.passesFilter(searchEvent))  {
                                
                                event = searchEvent;
                                break;
                            }
                        }
                        
                        if (event != null) {
                            AwsAccount account = AccountDao.getAccountByAcctNum(event.getRecipientAccountId());
                            ResourceDetailRequest request = new ResourceDetailRequest(account, event.getAwsRegion(), "EC2 Instance", value);
                            ResourceDetailDialog.showDialog(CloudTrailViewer.frame, request);
                        }
                    }
                    
                } else if (me.getClickCount() == 1) {
                    
                    try {    
                        eventTablePanel.setFilterString(value); 
                    } catch (Exception ex) {
                        LOGGER.log(Level.WARNING, "Problem responding to mouse event on chart table", ex);
                    }
                }
            }
        });
        
        TableColumn column;
        for (int i = 0; i < 3; i++) {
            column = table.getColumnModel().getColumn(i);

            switch (i) {
                case 0:
                    column.setMinWidth(15);
                    column.setMaxWidth(15);
                    column.setPreferredWidth(15);
                    break;

                case 2:
                    column.setMinWidth(70);
                    column.setMaxWidth(70);
                    column.setPreferredWidth(70);
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
        addCustomMenu();
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

    ////////////////////////////////////////////////////////////////////////////
    ///// Custom TableCellRenderer
    //////////////////////////////////////////////////////////////////////////// 
    private static class LegendColourRenderer extends JLabel implements TableCellRenderer {

        private static final long serialVersionUID = -6449877241312552646L;

        public LegendColourRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column) {

            Color newColor = (Color) color;
            setBackground(newColor);

            return this;
        }
    }
}
