package io.haskins.java.cloudtrailviewer.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by markhaskins on 26/01/2017.
 */
public class FileUtilsTests {

    @Test
    public void getApplicationDirectoryTest() {

        String expected = System.getProperty("user.home", ".") + "/.cloudtrailviewer/";
        String result = FileUtils.getApplicationDirectory();

        assertEquals(result, expected);
    }

    @Test
    public void getFullPathToFileTest() {

        String expected = System.getProperty("user.home", ".") + "/.cloudtrailviewer/filename.ext";
        String result = FileUtils.getFullPathToFile("filename.ext");

        assertEquals(result, expected);
    }

    @Test
    public void removeExtensionTest() {

        String expected = "filename";
        String result = FileUtils.removeExtension("filename.ext");

        assertEquals(result, expected);
    }
}
