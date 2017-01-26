package io.haskins.java.cloudtrailviewer.model.dao;

/**
 * Model that holds the latest version of the database configuration
 *
 * Created by markhaskins on 05/01/2017.
 */
public class CurrentDbVersion {

    private int dbVersion = 0;

    private  void setDbVersionInteger(Integer newVersion) {
        this.dbVersion = newVersion.intValue();
    }

    public void setDbVersion(int newVersion) {
        this.dbVersion = newVersion;
    }
    public int getDbVersion() {
        return this.dbVersion;
    }
}
