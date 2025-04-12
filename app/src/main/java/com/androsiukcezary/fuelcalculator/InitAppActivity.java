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

    static final boolean m_disableActivityForDebug = true;

    EditText m_initialPLNValueEditText;
    EditText m_currentFuelAmountEditText;
    DateTimeStruct m_dateTime;
    Button m_saveInitDataButton;

    InitDataSet m_validatedDataSet = new InitDataSet();

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
        m_currentFuelAmountEditText = (EditText) findViewById(R.id.currentFuelAmountEditText);

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

        if(m_disableActivityForDebug)
        {
            m_initialPLNValueEditText.setText("0.0"); /// DEBUG
            m_currentFuelAmountEditText.setText("0.0"); /// DEBUG
            m_saveInitDataButton.callOnClick(); /// DEBUG
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
            this.processCurrentFuelAmount();
            m_dateTime.processTimeValue(m_validatedDataSet.timeDateDataSet);
            m_dateTime.processDateValue(m_validatedDataSet.timeDateDataSet);

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
        m_validatedDataSet.validatedInitialPLNValue = parsedValue;
    }

    private void processCurrentFuelAmount() throws Exception {
        Log.i("INIT_APP_ACTIVITY_LOGS", "processCurrentFuelAmount");
        String currentFuelAmount = m_currentFuelAmountEditText.getText().toString().trim();
        Log.i("INIT_APP_ACTIVITY_LOGS", "currentFuelAmount value: " + currentFuelAmount);

        if (TextUtils.isEmpty(currentFuelAmount)) {
            m_currentFuelAmountEditText.setError("Field cannot be empty");
            m_currentFuelAmountEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // parse String to double
        double parsedValue;
        try {
            String normalizedInput = currentFuelAmount.replace(",", ".");
            parsedValue = Double.parseDouble(normalizedInput);
        } catch (NumberFormatException e) {
            m_currentFuelAmountEditText.setError("Cannot parse value to double");
            throw new Exception("parse to double failed");
        }

        // set data to variable
        m_validatedDataSet.validatedCurrentFuelAmount = parsedValue;
    }


    private void closeInitActivity(){
        Log.i("INIT_APP_ACTIVITY_LOGS", "closeInitActivity");
        // add data to return

        Intent returnIntent = new Intent();
        returnIntent.putExtra("INIT_DATA_SET", m_validatedDataSet); // class requires serializable

        setResult(RESULT_OK, returnIntent);
        finish();
    }
}