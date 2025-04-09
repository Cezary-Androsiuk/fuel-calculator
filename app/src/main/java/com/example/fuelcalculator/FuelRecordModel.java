package com.example.fuelcalculator;

public class FuelRecordModel {

    double m_fromFuel;
    double m_toFuel;

    public FuelRecordModel(double fromFuel, double toFuel) {
        this.m_fromFuel = fromFuel;
        this.m_toFuel = toFuel;
    }

    public double getFromFuel() {
        return m_fromFuel;
    }

    public double getToFuel() {
        return m_toFuel;
    }
}
