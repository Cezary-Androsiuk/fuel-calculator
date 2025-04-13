package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

import java.io.Serializable;

public class StartTripRecordModel extends FuelRecordModel implements Serializable {
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

    @Override
    public String toString() {
        return "StartTripRecordModel{" +
                "currentFuel=" + currentFuel +
                ", " + super.toString() +
                '}';
    }
}
