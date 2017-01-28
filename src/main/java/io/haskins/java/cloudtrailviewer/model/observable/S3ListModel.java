package io.haskins.java.cloudtrailviewer.model.observable;

import javafx.beans.property.*;

/**
 * Created by markhaskins on 27/01/2017.
 */
public class S3ListModel {

    public static final int FILE_BACK = 0;
    public static final int FILE_DIR = 1;
    public static final int FILE_DOC = 2;

    private final StringProperty name;
    private final ObjectProperty path;
    private final IntegerProperty fileType;

    public S3ListModel(String path, Object alias, int fileType) {

        this.name = new SimpleStringProperty(path);
        this.path = new SimpleObjectProperty(alias);
        this.fileType = new SimpleIntegerProperty(fileType);
    }

    public String getName() {
        return this.name.get();
    }

    public Object getPath() {
        return this.path.get();
    }

    public int getFileType() {
        return this.fileType.get();
    }
}
