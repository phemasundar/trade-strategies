package com.strategies.trade.utilities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */
public class Holidays {

    public static List<LocalDate> getHolidays2019() {
        ArrayList<LocalDate> listOfHolidays = new ArrayList<>();

//        listOfHolidays.add(LocalDate.of(2019, 2, 19));
        listOfHolidays.add(LocalDate.of(2019, 3, 4));
        listOfHolidays.add(LocalDate.of(2019, 3, 21));
//        listOfHolidays.add(LocalDate.of(2019, 4, 1));
        listOfHolidays.add(LocalDate.of(2019, 4, 17));
        listOfHolidays.add(LocalDate.of(2019, 4, 19));
        listOfHolidays.add(LocalDate.of(2019, 4, 29));
        listOfHolidays.add(LocalDate.of(2019, 5, 1));
        listOfHolidays.add(LocalDate.of(2019, 6, 5));
        listOfHolidays.add(LocalDate.of(2019, 8, 12));
        listOfHolidays.add(LocalDate.of(2019, 8, 15));
        listOfHolidays.add(LocalDate.of(2019, 9, 2));
        listOfHolidays.add(LocalDate.of(2019, 9, 10));
        listOfHolidays.add(LocalDate.of(2019, 10, 2));
        listOfHolidays.add(LocalDate.of(2019, 10, 8));
        listOfHolidays.add(LocalDate.of(2019, 10, 21));
        listOfHolidays.add(LocalDate.of(2019, 10, 28));
        listOfHolidays.add(LocalDate.of(2019, 11, 12));
        listOfHolidays.add(LocalDate.of(2019, 12, 25));

        return listOfHolidays;
    }

    public static List<LocalDate> getHolidays2020() {
        ArrayList<LocalDate> listOfHolidays = new ArrayList<>();

//        listOfHolidays.add(LocalDate.of(2020, 2, 19));
        listOfHolidays.add(LocalDate.of(2020, 2, 21));
        listOfHolidays.add(LocalDate.of(2020, 3, 10));
//        listOfHolidays.add(LocalDate.of(2020, 3, 25));
//        listOfHolidays.add(LocalDate.of(2020, 4, 1));
        listOfHolidays.add(LocalDate.of(2020, 4, 2));
        listOfHolidays.add(LocalDate.of(2020, 4, 6));
        listOfHolidays.add(LocalDate.of(2020, 4, 10));
        listOfHolidays.add(LocalDate.of(2020, 4, 14));
        listOfHolidays.add(LocalDate.of(2020, 5, 1));
//        listOfHolidays.add(LocalDate.of(2020, 5, 7));
        listOfHolidays.add(LocalDate.of(2020, 5, 25));
        listOfHolidays.add(LocalDate.of(2020, 10, 2));
//        listOfHolidays.add(LocalDate.of(2020, 10, 10));
        listOfHolidays.add(LocalDate.of(2020, 11, 16));
        listOfHolidays.add(LocalDate.of(2020, 11, 30));
        listOfHolidays.add(LocalDate.of(2020, 12, 25));

        return listOfHolidays;
    }

    public static List<LocalDate> getHolidays2021() {
        ArrayList<LocalDate> listOfHolidays = new ArrayList<>();

        listOfHolidays.add(LocalDate.of(2021, 1, 26));
//        listOfHolidays.add(LocalDate.of(2021, 2, 19));
        listOfHolidays.add(LocalDate.of(2021, 3, 11));
        listOfHolidays.add(LocalDate.of(2021, 3, 29));
//        listOfHolidays.add(LocalDate.of(2021, 4, 1));
        listOfHolidays.add(LocalDate.of(2021, 4, 2));
//        listOfHolidays.add(LocalDate.of(2021, 4, 13));
        listOfHolidays.add(LocalDate.of(2021, 4, 14));
        listOfHolidays.add(LocalDate.of(2021, 4, 21));
        listOfHolidays.add(LocalDate.of(2021, 5, 13));
//        listOfHolidays.add(LocalDate.of(2021, 5, 26));
        listOfHolidays.add(LocalDate.of(2021, 7, 21));
//        listOfHolidays.add(LocalDate.of(2021, 8, 16));
        listOfHolidays.add(LocalDate.of(2021, 8, 19));
        listOfHolidays.add(LocalDate.of(2021, 9, 10));
        listOfHolidays.add(LocalDate.of(2021, 10, 15));
//        listOfHolidays.add(LocalDate.of(2021, 10, 19));
//        listOfHolidays.add(LocalDate.of(2021, 11, 4));
        listOfHolidays.add(LocalDate.of(2021, 11, 5));
        listOfHolidays.add(LocalDate.of(2021, 11, 19));

        return listOfHolidays;
    }

    public static List<LocalDate> getHolidays() {
        List<LocalDate> holidays = getHolidays2019();
        holidays.addAll(getHolidays2020());
        holidays.addAll(getHolidays2021());
        return holidays;
    }
}
