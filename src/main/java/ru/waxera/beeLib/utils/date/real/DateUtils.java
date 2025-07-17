package ru.waxera.beeLib.utils.date.real;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static Timestamp getNow(){
        return Timestamp.valueOf(LocalDate.now().atStartOfDay());
    }

    public static Timestamp getLast(DayOfWeek target){
        LocalDate now = LocalDate.now();
        LocalDate lastTarget = now.with(TemporalAdjusters.previousOrSame(target));
        return Timestamp.valueOf(lastTarget.atStartOfDay());
    }

    public static double getDaysBetween(Timestamp start, Timestamp end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Timestamp не может быть null");
        }

        long diffMillis = end.getTime() - start.getTime();
        return (double) diffMillis / TimeUnit.DAYS.toMillis(1);
    }
}
