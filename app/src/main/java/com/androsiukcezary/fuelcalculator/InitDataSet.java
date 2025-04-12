package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;

import java.io.Serializable;

public class InitDataSet implements Serializable {

    double validatedInitialPLNValue = 0.0;
    double validatedCurrentFuelAmount = 0.0;

    TimeDateDataSet timeDateDataSet;

    public InitDataSet() {
        timeDateDataSet = new TimeDateDataSet();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString(){
        return String.format(
                "InitDataSet:{validatedInitialPLNValue: %.2f, validatedCurrentFuelAmount: %.2f, %s",
                validatedInitialPLNValue, validatedCurrentFuelAmount, timeDateDataSet);
    }
}
