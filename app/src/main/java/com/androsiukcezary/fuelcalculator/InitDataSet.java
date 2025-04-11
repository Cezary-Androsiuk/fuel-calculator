package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;

import java.io.Serializable;

public class InitDataSet implements Serializable {

    double validatedInitialPLNValue = 0.0;
    double validatedCurrentFuelAmount = 0.0;

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
                "InitDataSet:{validatedInitialPLNValue: %.2f, validatedCurrentFuelAmount: %.2f, " +
                        "year: %d, month: %d, day: %d, hour: %d, minute: %d, second: %d}",
                validatedInitialPLNValue, validatedCurrentFuelAmount,
                year, month, day, hour, minute, second);
    }
}
