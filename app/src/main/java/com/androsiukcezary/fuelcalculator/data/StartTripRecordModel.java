package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

public class StartTripRecordModel extends FuelRecordModel{
    private double currentFuel;

    public StartTripRecordModel(double currentFuel, TimeDateDataSet timeDateDataSet) {
        super(FuelRecordType.StartTripType, timeDateDataSet);
        this.currentFuel = currentFuel;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(double currentFuel) {
        this.currentFuel = currentFuel;
    }

}
