package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;

import java.io.Serializable;

public class TimeDateDataSet implements Serializable {
    public int second;
    public int minute;
    public int hour;

    public int day;
    public int month;
    public int year;

    @SuppressLint("DefaultLocale")
    @Override
    public String toString(){
        return String.format(
                "TimeDateDataSet:{year: %d, month: %d, day: %d, hour: %d, minute: %d, second: %d}",
                year, month, day, hour, minute, second);
    }
}
