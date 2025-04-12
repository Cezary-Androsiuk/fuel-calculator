package com.androsiukcezary.fuelcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddNewRefuelingRecord extends AppCompatActivity {

    EditText refueledQuantityEditText;
    EditText fuelPriceEditText;
    CheckBox otherCarUserPaysCheckBox;
    Button saveNewRefuelingButton;
    DateTimeStruct dateTime;

    double refueledQuantity;
    double fuelPrice;
    TimeDateDataSet timeDateDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_refueling_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton closeAddingRefuelingButton = (ImageButton) findViewById(R.id.closeAddingRefuelingButton);
        closeAddingRefuelingButton.setOnClickListener(v -> this.closeActivity());

        timeDateDataSet = new TimeDateDataSet();

        ///  store Views in variables
        refueledQuantityEditText = (EditText) findViewById(R.id.refueledQuantityEditText);
        fuelPriceEditText = (EditText) findViewById(R.id.fuelPriceEditText);
        otherCarUserPaysCheckBox = (CheckBox) findViewById(R.id.otherCarUserPaysCheckBox);
        saveNewRefuelingButton = (Button) findViewById(R.id.saveNewRefuelingButton);

        ///  create date time structure and assign views to them
        dateTime = new DateTimeStruct(
                this,
                findViewById(R.id.timeEditText),
                findViewById(R.id.timeSelectButton),
                findViewById(R.id.dateEditText),
                findViewById(R.id.dateSelectButton)
        );

        saveNewRefuelingButton.setOnClickListener(v -> this.onSaveNewRefuelingButtonClicked());
    }

    private void onSaveNewRefuelingButtonClicked(){
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "onSaveNewRefuelingButtonClicked");
        try{
            this.processRefueledQuantity();
            this.processFuelPrice();
            ///  No CheckBox process
            dateTime.processTimeValue(timeDateDataSet);
            dateTime.processDateValue(timeDateDataSet);

            this.saveActivity();
        } catch (Exception e) {
            Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "onSaveNewRefuelingButtonClicked exception: " + e);
        }

    }

    private void processRefueledQuantity() throws Exception{
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "processRefueledQuantity");
        String refueledQuantity = refueledQuantityEditText.getText().toString().trim();
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "refueledQuantity value: " + refueledQuantity);

        if (TextUtils.isEmpty(refueledQuantity)) {
            refueledQuantityEditText.setError("Field cannot be empty");
            refueledQuantityEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // parse String to double
        double parsedValue;
        try {
            String normalizedInput = refueledQuantity.replace(",", ".");
            parsedValue = Double.parseDouble(normalizedInput);
        } catch (NumberFormatException e) {
            refueledQuantityEditText.setError("Cannot parse value to double");
            throw new Exception("parse to double failed");
        }

        // set data to variable
        this.refueledQuantity = parsedValue;
    }

    private void processFuelPrice() throws Exception{
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "processFuelPrice");
        String fuelPrice = fuelPriceEditText.getText().toString().trim();
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "fuelPrice value: " + fuelPrice);

        if (TextUtils.isEmpty(fuelPrice)) {
            fuelPriceEditText.setError("Field cannot be empty");
            fuelPriceEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // parse String to double
        double parsedValue;
        try {
            String normalizedInput = fuelPrice.replace(",", ".");
            parsedValue = Double.parseDouble(normalizedInput);
        } catch (NumberFormatException e) {
            fuelPriceEditText.setError("Cannot parse value to double");
            throw new Exception("parse to double failed");
        }

        // set data to variable
        this.fuelPrice = parsedValue;
    }

    private void saveActivity(){
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "saveActivity");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("REFUELED_QUANTITY", refueledQuantity);
        returnIntent.putExtra("FUEL_PRICE", fuelPrice);
        returnIntent.putExtra("OTHER_CAR_USER_PAYS", otherCarUserPaysCheckBox.isChecked());
        returnIntent.putExtra("TIME_DATA_DATA_SET", timeDateDataSet); // class requires serializable

        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void closeActivity(){
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "closeActivity");
        setResult(RESULT_CANCELED);
        finish();
    }
}