package net.sydokiddo.chrysalis.misc.util.helpers;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class DateHelper {

    /**
     * Gets specific dates for various methods that only trigger on said specific date.
     **/

    public static final LocalDate localDate = LocalDate.now();
    public static final int getMonth = localDate.getMonth().getValue();
    public static final int getDay = localDate.getDayOfMonth();

    public static boolean isCustomDate(int month, int day) {
        return getMonth == month && getDay == day;
    }

    public static boolean isHalloween() {
        return getMonth == 10 && getDay == 31;
    }

    public static boolean isChristmas() {
        return getMonth == 12 && getDay == 25;
    }

    public static boolean isAprilFools() {
        return getMonth == 4 && getDay == 1;
    }
}