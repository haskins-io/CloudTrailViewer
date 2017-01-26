package io.haskins.java.cloudtrailviewer.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for converting Date/Time String representations to other formats.
 *
 * Created by markhaskins on 05/01/2017.
 */
class DateFormatter {

    private final ThreadLocal<DateFormat> df_with_zone = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        }

        @Override
        public void set(DateFormat value) {
            super.set(value);
        }
    };

    long convertStringToLong(String dateString) throws ParseException {
        return df_with_zone.get().parse(dateString).getTime();
    }

    Date convertStringToDate(String dateString) throws ParseException {
        return df_with_zone.get().parse(dateString);
    }
}
