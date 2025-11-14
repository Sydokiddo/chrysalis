package net.junebug.chrysalis.util.helpers;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class DateHelper {

    /**
     * Gets specific dates for various methods that only trigger on said specific date.
     **/

    public static boolean isDate(int month, int day) {
        LocalDate localDate = LocalDate.now();
        return localDate.getMonth().getValue() == month && localDate.getDayOfMonth() == day;
    }

    public static boolean isAprilFools() {
        return isDate(4, 1);
    }

    public static boolean isHalloween() {
        return isDate(10, 31);
    }

    public static boolean isChristmas() {
        return isDate(12, 25);
    }
}