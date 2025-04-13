package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

import java.io.Serializable;

public class EndTripRecordModel extends FuelRecordModel implements Serializable {
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

    @Override
    public String toString() {
        return "EndTripRecordModel{" +
                "currentFuel=" + currentFuel +
                ", " + super.toString() +
                '}';
    }
}
