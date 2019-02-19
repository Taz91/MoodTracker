package com.agilya.syc.moodtracker;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String formatDate(Date pDate){
        final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
        Date today = new Date();

        long interval = (pDate.getTime() - today.getTime()) / MILLISECONDS_PER_DAY;
        switch ( (int) Math.abs(interval)) {
            case 1:
                return "Hier.";
            case 2:
                return "Avant-hier.";
            case 3: case 4: case 5: case 6:
                return "Il y a " + Math.abs(interval) + " jours.";
            case 7:
                return "Il y a une semaine.";
            default:
                return DateFormat.getDateInstance( DateFormat.MEDIUM, Locale.FRANCE ).format(pDate);
        }
    }


}
