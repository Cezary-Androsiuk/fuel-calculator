package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

public class PaymentRecordModel extends FuelRecordModel{
    double moneyPaid;

    public PaymentRecordModel(
            double moneyPaid, TimeDateDataSet timeDateDataSet)
    {
        super(FuelRecordType.PaymentType, timeDateDataSet);
        this.moneyPaid = moneyPaid;
    }

    public double getMoneyPaid() {
        return moneyPaid;
    }

    public void setMoneyPaid(double moneyPaid) {
        this.moneyPaid = moneyPaid;
    }
}
