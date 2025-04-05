package net.sydokiddo.chrysalis.util.helpers;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class DateHelper {

    /**
     * Gets specific dates for various methods that only trigger on said specific date.
     **/

    private static final LocalDate localDate = LocalDate.now();

    private static final int
        getMonth = localDate.getMonth().getValue(),
        getDay = localDate.getDayOfMonth()
    ;

    public static boolean isDate(int month, int day) {
        return getMonth == month && getDay == day;
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