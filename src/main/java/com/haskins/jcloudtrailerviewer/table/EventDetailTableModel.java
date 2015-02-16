package com.haskins.jcloudtrailerviewer.table;

import com.haskins.jcloudtrailerviewer.model.Event;
import java.io.IOException;
import javax.swing.table.AbstractTableModel;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mark.haskins
 */
public class EventDetailTableModel extends AbstractTableModel {

    private final ObjectMapper mapper = new ObjectMapper();

    private Event detailEvent = null;

    public void showDetail(Event event) {

        detailEvent = event;
        fireTableRowsInserted(1, 2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // AbstractTableModel implementation
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        int retVal = 0;

        if (detailEvent != null) {
            retVal = 19;
        }

        return retVal;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = "";

        switch (rowIndex) {

            case 0:
                value = "Event Version";
                if (columnIndex == 1) {
                    value = detailEvent.getEventVersion();
                }
                break;
            case 1:
                value = "User Identity";
                if (columnIndex == 1) {

                    value = "";
                    if (detailEvent.getUserIdentity() != null) {
                        try {
                            value = mapper.defaultPrettyPrintingWriter().writeValueAsString(detailEvent.getUserIdentity());
                        } catch (IOException ex) {
                            detailEvent.setRawUserIdentity("");
                        }
                    }
                }
                break;
            case 2:
                value = "Event Time";
                if (columnIndex == 1) {
                    value = detailEvent.getEventTime();
                }
                break;
            case 3:
                value = "Event Source";
                if (columnIndex == 1) {
                    value = detailEvent.getEventSource();
                }
                break;
            case 4:
                value = "Event Name";
                if (columnIndex == 1) {
                    value = detailEvent.getEventName();
                }
                break;
            case 5:
                value = "AWS Region";
                if (columnIndex == 1) {
                    value = detailEvent.getAwsRegion();
                }
                break;
            case 6:
                value = "Source IP";
                if (columnIndex == 1) {
                    value = detailEvent.getSourceIPAddress();
                }
                break;
            case 7:
                value = "User Agent";
                if (columnIndex == 1) {
                    value = detailEvent.getUserAgent();
                }
                break;
            case 8:
                value = "Request Parameters";
                if (columnIndex == 1) {
                    value = "";
                    if (detailEvent.getRequestParameters() != null) {
                        try {
                            value = mapper.defaultPrettyPrintingWriter().writeValueAsString(detailEvent.getRequestParameters());
                        } catch (IOException ex) {
                            detailEvent.setRawUserIdentity("");
                        }
                    }
                }
                break;
            case 9:
                value = "Response Elements";
                if (columnIndex == 1) {
                    value = "";
                    if (detailEvent.getResponseElements() != null) {
                        try {
                            value = mapper.defaultPrettyPrintingWriter().writeValueAsString(detailEvent.getResponseElements());
                        } catch (IOException ex) {
                            detailEvent.setRawUserIdentity("");
                        }
                    }
                }
                break;
            case 10:
                value = "Session Issuer";
                if (columnIndex == 1) {
                    value = "";
                    if (detailEvent.getSessionIssuer() != null) {
                        try {
                            value = mapper.defaultPrettyPrintingWriter().writeValueAsString(detailEvent.getSessionIssuer());
                        } catch (IOException ex) {
                            detailEvent.setRawUserIdentity("");
                        }
                    }
                }
                break;
            case 11:
                value = "Request Id";
                if (columnIndex == 1) {
                    value = detailEvent.getRequestId();
                }
                break;
            case 12:
                value = "Event Type";
                if (columnIndex == 1) {
                    value = detailEvent.getEventType();
                }
                break;
            case 13:
                value = "Recipient Account";
                if (columnIndex == 1) {
                    value = detailEvent.getRecipientAccountId();
                }
                break;
            case 14:
                value = "Error Code";
                if (columnIndex == 1) {
                    value = detailEvent.getErrorCode();
                }
                break;
            case 15:
                value = "Error Message";
                if (columnIndex == 1) {
                    value = detailEvent.getErrorMessage();
                }
                break;
            case 16:
                value = "Additional Event Data";
                if (columnIndex == 1) {
                    value = "";
                    if (detailEvent.getAdditionalEventData() != null) {
                        try {
                            value = mapper.defaultPrettyPrintingWriter().writeValueAsString(detailEvent.getAdditionalEventData());
                        } catch (IOException ex) {
                            detailEvent.setRawUserIdentity("");
                        }
                    }
                }
                break;
            case 17:
                value = "Read Only";
                if (columnIndex == 1) {
                    value = detailEvent.getReadOnly();
                }
                break;
            case 18:
                value = "Resources";
                if (columnIndex == 1) {
                    value = "";
                    if (detailEvent.getResources() != null) {
                        try {
                            value = mapper.defaultPrettyPrintingWriter().writeValueAsString(detailEvent.getResources());
                        } catch (IOException ex) {
                            detailEvent.setRawUserIdentity("");
                        }
                    }
                }
                break;
        }

        return value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private final static String[] columnNames = new String[]{
        "Property", "Value"
    };
}
