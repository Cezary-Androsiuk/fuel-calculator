package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

import java.io.Serializable;

public class FirstRecordModel extends FuelRecordModel implements Serializable {
    private double initialPLNValue;
    private double currentFuel;
    private double currentFuelPrice;

    public FirstRecordModel(double initialPLNValue, double currentFuel,
            double currentFuelPrice, TimeDateDataSet timeDateDataSet) {
        super(FuelRecordType.FirstRecordType, timeDateDataSet);
        this.initialPLNValue = initialPLNValue;
        this.currentFuel = currentFuel;
        this.currentFuelPrice = currentFuelPrice;
    }

    public double getInitialPLNValue() {
        return initialPLNValue;
    }

    public void setInitialPLNValue(double initialPLNValue) {
        this.initialPLNValue = initialPLNValue;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(double currentFuel) {
        this.currentFuel = currentFuel;
    }

    public double getCurrentFuelPrice() {
        return currentFuelPrice;
    }

    public void setCurrentFuelPrice(double currentFuelPrice) {
        this.currentFuelPrice = currentFuelPrice;
    }

    @Override
    public String toString() {
        return "FirstRecordModel{" +
                "initialPLNValue=" + initialPLNValue +
                ", currentFuel=" + currentFuel +
                ", currentFuelPrice=" + currentFuelPrice +
                ", " + super.toString() +
                '}';
    }
}
