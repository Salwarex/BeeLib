package ru.waxera.beeLib.utils.date.real;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class DateFormatUtils {
    public static String format(Timestamp timestamp, String format){
        if (timestamp == null) return null;
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(format));
    }
}
