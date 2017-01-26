package io.haskins.java.cloudtrailviewer.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Collection of File / Directory related functionality.
 *
 * Created by markhaskins on 25/01/2017.
 */
public class FileUtils {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    /**
     * Returns the full path to the CloudTrail Viewer directory within the System defined user.home.
     *
     * @return Full path to cloudtrailviewer directory ending in a forward slash '/'
     */
    public static String getApplicationDirectory() {

        return System.getProperty("user.home", ".") + "/.cloudtrailviewer/";
    }

    public static String getFullPathToFile(String pathToFile) {
        return getApplicationDirectory() + pathToFile;
    }

    public static Boolean doesFileExist(String pathToFile) {

        File f = new File(pathToFile);
        return f.exists();
    }

    public static String getFileAsString(String pathToFile) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pathToFile)), StandardCharsets.UTF_8);
    }

    public static boolean writeStringToFile(String content, String pathToFile) {

        boolean written = false;

        try (BufferedWriter out = new BufferedWriter(new FileWriter(pathToFile))) {
            out.write(content);
            out.close();
            written = true;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to write String to File", e);
        }

        return written;
    }

    public static String removeExtension(String filename) {
        return filename.replaceFirst("[.][^.]+$", "");
    }

}
