package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InitAppActivity extends AppCompatActivity {

    EditText m_initialPLNValueEditText;
    EditText m_currentFuelEditText;
    EditText m_currentFuelPriceEditText;
    DateTimeStruct m_dateTime;
    Button m_saveInitDataButton;

    double validatedInitialPLNValue = 0.0;
    double validatedCurrentFuel = 0.0;
    double validatedCurrentFuelPrice = 0.0;

    TimeDateDataSet timeDateDataSet = new TimeDateDataSet();

    // prevents restarting while device changed position form vertical to horizontal
    boolean m_activityInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("INIT_APP_ACTIVITY_LOGS", "onCreate");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_init_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        // remove later - after adding vertically scrollable interface
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(m_activityInitialized)
            return;

        ///  assign initial PLN and current fuel views to variables
        m_initialPLNValueEditText = (EditText) findViewById(R.id.initialPLNValueEditText);
        m_currentFuelEditText = (EditText) findViewById(R.id.currentFuelEditText);
        m_currentFuelPriceEditText = (EditText) findViewById(R.id.currentFuelPriceEditText);

        ///  create date time structure and assign views to them
        m_dateTime = new DateTimeStruct(
                this,
                findViewById(R.id.timeEditText),
                findViewById(R.id.timeSelectButton),
                findViewById(R.id.dateEditText),
                findViewById(R.id.dateSelectButton)
        );


        ///  assign save init button to variable
        m_saveInitDataButton = (Button) findViewById(R.id.saveInitDataButton);
        ///  connect onClick function with save button
        m_saveInitDataButton.setOnClickListener(v -> this.onSaveInitDataButtonClicked());

        if(SettingsActivity.speedUpFormsForDebug)
        {
            m_initialPLNValueEditText.setText("0.0"); /// DEBUG
            m_currentFuelEditText.setText("0.0"); /// DEBUG
            m_currentFuelPriceEditText.setText("0.0"); /// DEBUG
//            m_saveInitDataButton.callOnClick(); /// DEBUG
        }

        m_activityInitialized = true;
    }

    @Override
    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        /// don't call super.onBackPressed() to disable closing this Activity
         finishAffinity();
    }

    ///  Called while user press Save (initial data)
    public void onSaveInitDataButtonClicked(){
        Log.i("INIT_APP_ACTIVITY_LOGS", "onSaveInitDataButtonClicked");
        try{
            this.processInitialPLNValue();
            this.processCurrentFuel();
            this.processCurrentFuelPrice();
            m_dateTime.processTimeValue(timeDateDataSet);
            m_dateTime.processDateValue(timeDateDataSet);

            this.closeInitActivity();
        } catch (Exception e) {
            Log.i("INIT_APP_ACTIVITY_LOGS", "onSaveInitDataButtonClicked exception: " + e);
        }
    }

    private void processInitialPLNValue() throws Exception {
        Log.i("INIT_APP_ACTIVITY_LOGS", "processInitialPLNValue");
        String initialPLNValue = m_initialPLNValueEditText.getText().toString().trim();
        Log.i("INIT_APP_ACTIVITY_LOGS", "initialPLNValue1 value: " + initialPLNValue);

        if (TextUtils.isEmpty(initialPLNValue)) {
            m_initialPLNValueEditText.setError("Field cannot be empty");
            m_initialPLNValueEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // parse String to double
        double parsedValue;
        try {
            String normalizedInput = initialPLNValue.replace(",", ".");
            parsedValue = Double.parseDouble(normalizedInput);
        } catch (NumberFormatException e) {
            m_initialPLNValueEditText.setError("Cannot parse value to double");
            throw new Exception("parse to double failed");
        }

        // set data to variable
        this.validatedInitialPLNValue = parsedValue;
    }

    private void processCurrentFuel() throws Exception {
        Log.i("INIT_APP_ACTIVITY_LOGS", "processCurrentFuel");
        String currentFuel = m_currentFuelEditText.getText().toString().trim();
        Log.i("INIT_APP_ACTIVITY_LOGS", "processCurrentFuel value: " + currentFuel);

        if (TextUtils.isEmpty(currentFuel)) {
            m_currentFuelEditText.setError("Field cannot be empty");
            m_currentFuelEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // parse String to double
        double parsedValue;
        try {
            String normalizedInput = currentFuel.replace(",", ".");
            parsedValue = Double.parseDouble(normalizedInput);
        } catch (NumberFormatException e) {
            m_currentFuelEditText.setError("Cannot parse value to double");
            throw new Exception("parse to double failed");
        }

        // set data to variable
        this.validatedCurrentFuel = parsedValue;
    }

    private void processCurrentFuelPrice() throws Exception {
        Log.i("INIT_APP_ACTIVITY_LOGS", "processCurrentFuelPrice");
        String currentFuelPrice = m_currentFuelPriceEditText.getText().toString().trim();
        Log.i("INIT_APP_ACTIVITY_LOGS", "processCurrentFuelPrice value: " + currentFuelPrice);

        if (TextUtils.isEmpty(currentFuelPrice)) {
            m_currentFuelPriceEditText.setError("Field cannot be empty");
            m_currentFuelPriceEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // parse String to double
        double parsedValue;
        try {
            String normalizedInput = currentFuelPrice.replace(",", ".");
            parsedValue = Double.parseDouble(normalizedInput);
        } catch (NumberFormatException e) {
            m_currentFuelPriceEditText.setError("Cannot parse value to double");
            throw new Exception("parse to double failed");
        }

        // set data to variable
        this.validatedCurrentFuelPrice = parsedValue;
    }


    private void closeInitActivity(){
        Log.i("INIT_APP_ACTIVITY_LOGS", "closeInitActivity");
        // add data to return

        Intent returnIntent = new Intent();
        returnIntent.putExtra("INITIAL_PLN_VALUE", validatedInitialPLNValue);
        returnIntent.putExtra("CURRENT_FUEL", validatedCurrentFuel);
        returnIntent.putExtra("CURRENT_FUEL_PRICE", validatedCurrentFuelPrice);
        returnIntent.putExtra("TIME_DATA_DATA_SET", timeDateDataSet); // class requires serializable

        setResult(RESULT_OK, returnIntent);
        finish();
    }
}