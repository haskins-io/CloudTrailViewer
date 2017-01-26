package io.haskins.java.cloudtrailviewer.model;

/**
 * Created by markhaskins on 06/01/2017.
 */
public class DialogAction {

    public static final int ACTION_CANCEL = 0;
    public static final int ACTION_OK = 1;

    private int actionCode;
    private Object actionPayload;

    public DialogAction() {
        this(ACTION_CANCEL, null);
    }

    public DialogAction(int action, Object payload) {

        setActionCode(action);
        setActionPayload(payload);
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public Object getActionPayload() {
        return actionPayload;
    }

    public void setActionPayload(Object actionPayload) {
        this.actionPayload = actionPayload;
    }
}
