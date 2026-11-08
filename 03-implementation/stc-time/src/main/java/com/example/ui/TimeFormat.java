package com.example.ui;

public final class TimeFormat {

    private TimeFormat() {}

    /** 750 -> "12h 30min", -90 -> "-1h 30min" */
    public static String plain(int minutes) {
        String sign = minutes < 0 ? "-" : "";
        int abs = Math.abs(minutes);
        return sign + (abs / 60) + "h " + String.format("%02d", abs % 60) + "min";
    }

    /** 750 -> "+12h 30min" */
    public static String signed(int minutes) {
        return (minutes > 0 ? "+" : "") + plain(minutes);
    }
}