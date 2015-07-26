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
package com.haskins.cloudtrailviewer.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author mark
 */
public class GeneralUtils {

    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
        List<T> list = new ArrayList<>(c);
        java.util.Collections.sort(list);
        return list;
    }

    public enum OS {

        WINDOWS, UNIX, POSIX_UNIX, MAC, OTHER
    }

    private static OS os = OS.OTHER;

    static {
        try {

            String osName = System.getProperty("os.name");
            if (osName == null) {
                throw new IOException("os.name not found");
            }

            osName = osName.toLowerCase(Locale.ENGLISH);
            if (osName.contains("windows")) {
                os = OS.WINDOWS;

            }
            else if (osName.contains("linux")
                || osName.contains("mpe/ix")
                || osName.contains("freebsd")
                || osName.contains("irix")
                || osName.contains("digital unix")
                || osName.contains("unix")) {
                os = OS.UNIX;

            }
            else if (osName.contains("mac os x")) {
                os = OS.MAC;

            }
            else if (osName.contains("sun os")
                || osName.contains("sunos")
                || osName.contains("solaris")) {
                os = OS.POSIX_UNIX;

            }
            else if (osName.contains("hp-ux")
                || osName.contains("aix")) {
                os = OS.POSIX_UNIX;

            }
            else {
                os = OS.OTHER;
            }

        }
        catch (Exception ex) {
            os = OS.OTHER;
        }
    }

    public static OS getOs() {
        return os;
    }

    public static boolean isMac() {
        boolean isMac = false;

        if (GeneralUtils.getOs().equals(GeneralUtils.OS.MAC)) {
            isMac = true;
        }

        return isMac;
    }
}
