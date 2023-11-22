package net.sydokiddo.chrysalis.misc.util;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

@SuppressWarnings("unused")
public interface DateHelper {

    /**
     * Gets specific dates for various methods that only trigger on said specific date.
     **/

    LocalDate localDate = LocalDate.now();
    int getMonth = localDate.get(ChronoField.MONTH_OF_YEAR);
    int getDay = localDate.get(ChronoField.DAY_OF_MONTH);

    default boolean isCustomDate(int month, int day) {
        return getMonth == month && getDay == day;
    }

    default boolean isHalloween() {
        return getMonth == 10 && getDay == 31;
    }

    default boolean isChristmas() {
        return getMonth == 12 && getDay == 25;
    }

    default boolean isAprilFools() {
        return getMonth == 4 && getDay == 1;
    }
}