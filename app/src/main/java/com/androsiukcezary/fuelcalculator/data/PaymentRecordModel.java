package com.androsiukcezary.fuelcalculator.data;

import com.androsiukcezary.fuelcalculator.TimeDateDataSet;

import java.io.Serializable;

public class PaymentRecordModel extends FuelRecordModel implements Serializable {
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

    @Override
    public String toString() {
        return "PaymentRecordModel{" +
                "moneyPaid=" + moneyPaid +
                ", " + super.toString() +
                '}';
    }
}
