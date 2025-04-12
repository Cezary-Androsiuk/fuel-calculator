package com.androsiukcezary.fuelcalculator;

import android.content.Intent;
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

public class AddNewStartTripRecord extends AppCompatActivity {

    EditText m_currentFuelEditText;
    Button m_saveNewStartTripButton;
    DateTimeStruct m_dateTime;

    double m_currentFuel;
    TimeDateDataSet m_timeDateDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ADD_NEW_START_TRIP_RECORD_ACTIVITY_LOGS", "onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_start_new_trip_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton closeAddingStartTripButton = (ImageButton) findViewById(R.id.closeAddingStartTripButton);
        closeAddingStartTripButton.setOnClickListener(v -> this.cancelActivity());

        m_timeDateDataSet = new TimeDateDataSet();

        ///  store Views in variables
        m_currentFuelEditText = (EditText) findViewById(R.id.currentFuelEditText);
        m_saveNewStartTripButton = (Button) findViewById(R.id.saveNewStartTripButton);

        ///  create date time structure and assign views to them
        m_dateTime = new DateTimeStruct(
                this,
                findViewById(R.id.timeEditText),
                findViewById(R.id.timeSelectButton),
                findViewById(R.id.dateEditText),
                findViewById(R.id.dateSelectButton)
        );

        m_saveNewStartTripButton.setOnClickListener(v -> onSaveNewStartTripButtonClicked());

    }

    private void onSaveNewStartTripButtonClicked(){
        Log.i("ADD_NEW_START_TRIP_RECORD_ACTIVITY_LOGS", "onSaveNewStartTripButtonClicked");
        try{
            this.processCurrentFuel();
            m_dateTime.processTimeValue(m_timeDateDataSet);
            m_dateTime.processDateValue(m_timeDateDataSet);

            this.saveActivity();
        } catch (Exception e) {
            Log.i("ADD_NEW_START_TRIP_RECORD_ACTIVITY_LOGS", "onSaveNewStartTripButtonClicked exception: " + e);
        }
    }

    private void processCurrentFuel() throws Exception{
        Log.i("ADD_NEW_START_TRIP_RECORD_ACTIVITY_LOGS", "processCurrentFuel");
        String currentFuel = m_currentFuelEditText.getText().toString().trim();
        Log.i("ADD_NEW_START_TRIP_RECORD_ACTIVITY_LOGS", "currentFuel value: " + currentFuel);

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
        m_currentFuel = parsedValue;
    }

    private void saveActivity(){
        Log.i("ADD_NEW_START_TRIP_RECORD_ACTIVITY_LOGS", "saveActivity");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("CURRENT_FUEL", m_currentFuel);
        returnIntent.putExtra("TIME_DATA_DATA_SET", m_timeDateDataSet); // class requires serializable

        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void cancelActivity(){
        Log.i("ADD_NEW_START_TRIP_RECORD_ACTIVITY_LOGS", "cancelActivity");
        setResult(RESULT_CANCELED);
        finish();
    }
}