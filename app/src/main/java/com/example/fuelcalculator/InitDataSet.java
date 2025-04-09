package com.example.fuelcalculator;

import java.io.Serializable;

public class InitDataSet implements Serializable {

    double validatedInitialPLNValue = 0.0;
    double validatedCurrentFuelAmount = 0.0;

    public int second;
    public int minute;
    public int hour;

    public int month;
    public int day;
    public int year;
}
