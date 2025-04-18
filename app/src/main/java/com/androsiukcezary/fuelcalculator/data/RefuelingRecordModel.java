package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

import java.io.Serializable;

public class RefuelingRecordModel extends FuelRecordModel implements Serializable {
    private double refueledQuantity;
    private double fuelPrice;
    private boolean otherCarUserPays;

    public RefuelingRecordModel(
            double refueledQuantity, double fuelPrice,
            boolean otherCarUserPays, TimeDateDataSet timeDateDataSet)
    {
        super(FuelRecordType.RefuelingType, timeDateDataSet);
        this.refueledQuantity = refueledQuantity;
        this.fuelPrice = fuelPrice;
        this.otherCarUserPays = otherCarUserPays;
    }

    public double getRefueledQuantity() {
        return refueledQuantity;
    }

    public void setRefueledQuantity(double refueledQuantity) {
        this.refueledQuantity = refueledQuantity;
    }

    public double getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(double fuelPrice) {
        this.fuelPrice = fuelPrice;
    }

    public boolean isOtherCarUserPays() {
        return otherCarUserPays;
    }

    public void setOtherCarUserPays(boolean otherCarUserPays) {
        this.otherCarUserPays = otherCarUserPays;
    }

    @Override
    public String toString() {
        return "RefuelingRecordModel{" +
                "refueledQuantity=" + refueledQuantity +
                ", fuelPrice=" + fuelPrice +
                ", otherCarUserPays=" + otherCarUserPays +
                ", " + super.toString() +
                '}';
    }
}
