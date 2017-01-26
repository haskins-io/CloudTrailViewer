package io.haskins.java.cloudtrailviewer.model.dao;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 *
 * Created by markhaskins on 05/01/2017.
 */
public class ResultSetRow extends LinkedHashMap {

    private static final long serialVersionUID = 5573616437008138154L;

    /**
     * Default constructor
     */
    public ResultSetRow() {
        super();
    }

    public ResultSetRow(int initialCapacity) {
        super(initialCapacity);
    }

    public ResultSetRow(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ResultSetRow(Map m) {
        super(m);
    }

    public Object get(String key)  {

        if (super.get(key) == null) {
            return super.get(key.toUpperCase());
        }

        return super.get(key);
    }
}

