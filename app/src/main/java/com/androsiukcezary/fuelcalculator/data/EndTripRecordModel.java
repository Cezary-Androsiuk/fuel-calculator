package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

public class EndTripRecordModel extends FuelRecordModel{
    private double currentFuel;

    public EndTripRecordModel(double currentFuel, TimeDateDataSet timeDateDataSet) {
        super(FuelRecordType.EndTripType, timeDateDataSet);
        this.currentFuel = currentFuel;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(double currentFuel) {
        this.currentFuel = currentFuel;
    }

}
