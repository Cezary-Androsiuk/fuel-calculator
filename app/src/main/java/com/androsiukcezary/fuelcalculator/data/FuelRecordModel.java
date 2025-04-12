package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

public abstract class FuelRecordModel {

    private FuelRecordType fuelRecordType;

    private TimeDateDataSet timeDateDataSet;

    public FuelRecordModel(FuelRecordType fuelRecordType, TimeDateDataSet timeDateDataSet) {
        this.fuelRecordType = fuelRecordType;
        this.timeDateDataSet = timeDateDataSet;
    }

    public FuelRecordType getFuelRecordType(){
        return fuelRecordType;
    }

    public TimeDateDataSet getTimeDateDataSet() {
        return timeDateDataSet;
    }

    public void setTimeDateDataSet(TimeDateDataSet timeDateDataSet) {
        this.timeDateDataSet = timeDateDataSet;
    }
}
