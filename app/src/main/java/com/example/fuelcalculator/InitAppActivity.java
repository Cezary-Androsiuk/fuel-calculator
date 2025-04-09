package com.example.fuelcalculator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

    public class DateTimeStruct{
        public EditText timeEditText;
        public ImageButton timeSelectButton;

        public EditText dateEditText;
        public ImageButton dateSelectButton;
    }

    EditText m_initialPLNValueEditText;
    EditText m_currentFuelAmountEditText;
    DateTimeStruct m_dateTime;
    Button m_saveInitDataButton;

    InitDataSet m_validatedDataSet = new InitDataSet();

    final String c_timePattern = "HH:mm:ss";
    final String c_datePattern = "yyyy/MM/dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_init_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ///  assign initial PLN and current fuel views to variables
        m_initialPLNValueEditText = (EditText) findViewById(R.id.initialPLNValueEditText);
        m_initialPLNValueEditText.setText("0.0");
        m_currentFuelAmountEditText = (EditText) findViewById(R.id.currentFuelAmountEditText);
        m_currentFuelAmountEditText.setText("0.0");



        ///  create structure and assign views to them
        m_dateTime = new DateTimeStruct();
        m_dateTime.timeEditText = (EditText) findViewById(R.id.timeEditText);
        m_dateTime.timeSelectButton = (ImageButton) findViewById(R.id.timeSelectButton);
        m_dateTime.dateEditText = (EditText) findViewById(R.id.dateEditText);
        m_dateTime.dateSelectButton = (ImageButton) findViewById(R.id.dateSelectButton);

        ///  assign initial time
        SimpleDateFormat timeFormat = new SimpleDateFormat(c_timePattern, Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        m_dateTime.timeEditText.setText(currentTime);
        ///  connect onClick function with button
        m_dateTime.timeSelectButton.setOnClickListener(v -> this.openTimeSelector());

        ///  assign initial date
        SimpleDateFormat dateFormat = new SimpleDateFormat(c_datePattern, Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        m_dateTime.dateEditText.setText(currentDate);
        ///  connect onClick function with button
        m_dateTime.dateSelectButton.setOnClickListener(v -> this.openDateSelector());



        ///  assign save init button to variable
        m_saveInitDataButton = (Button) findViewById(R.id.saveInitDataButton);
        ///  connect onClick function with save button
        m_saveInitDataButton.setOnClickListener(v -> this.onSaveInitDataButtonClicked());
    }

    @Override
    public void onBackPressed() {
        /// don't call super.onBackPressed() to disable closing this Activity
        finishAffinity();
    }

    ///  Open while changing the time
    public void openTimeSelector(){
        ///  read necessary data from calendar
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        ///  optional addition by reading currently set time and only if fails, then use current
//        int hour = currentHour;
//        int minute = currentMinute;

        new TimePickerDialog(InitAppActivity.this, (view, initHour, initMinute) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d:00", initHour, initMinute);
            m_dateTime.timeEditText.setText(time);
        }, currentHour, currentMinute, true).show();
    }

    ///  Open while changing the date
    public void openDateSelector(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfM = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                InitAppActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(),
                            "%04d/%02d/%02d", year, month + 1, dayOfMonth);
                    m_dateTime.dateEditText.setText(selectedDate);
                }, currentYear, currentMonth, currentDayOfM
        );
        datePickerDialog.show();
    }

    ///  Called while user press Save (initial data)
    public void onSaveInitDataButtonClicked(){
        try{
            this.processInitialPLNValue();
            this.processCurrentFuelAmount();
            this.processTimeValue();
            this.processDateValue();

            // log success
            this.closeInitActivity();
        } catch (Exception e) {
            // log failed
        }
    }

    private void processInitialPLNValue() throws Exception {
        String initialPLNValue = m_initialPLNValueEditText.getText().toString().trim();
        Log.i("INITIAL_CONFIGURATION", "initialPLNValue1 value: " + initialPLNValue);

        if (TextUtils.isEmpty(initialPLNValue)) {
            m_initialPLNValueEditText.setError("Field cannot be empty");
            m_initialPLNValueEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }
        Log.i("INITIAL_CONFIGURATION", "initialPLNValue2 value: " + initialPLNValue);

        // parse String to double
        double parsedValue;
        try {
            Log.i("INITIAL_CONFIGURATION", "initialPLNValue3 value: " + initialPLNValue);
            String normalizedInput = initialPLNValue.replace(",", ".");
            parsedValue = Double.parseDouble(normalizedInput);
            Log.i("INITIAL_CONFIGURATION", "initialPLNValue4 value: " + initialPLNValue);
        } catch (NumberFormatException e) {
            m_initialPLNValueEditText.setError("Cannot parse value to double");
            throw new Exception("parse to double failed");
        }
        Log.i("INITIAL_CONFIGURATION", "initialPLNValue5 value: " + initialPLNValue);

        // set data to variable
        m_validatedDataSet.validatedInitialPLNValue = parsedValue;
    }

    private void processCurrentFuelAmount() throws Exception {
        String currentFuelAmount = m_currentFuelAmountEditText.getText().toString().trim();
        Log.i("INITIAL_CONFIGURATION", "currentFuelAmount value: " + currentFuelAmount);

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

    private void processTimeValue() throws Exception {
        String timeInput = m_dateTime.timeEditText.getText().toString().trim();
        Log.i("INITIAL_CONFIGURATION", "timeInput value: " + timeInput);

        if (TextUtils.isEmpty(timeInput)) {
            m_dateTime.timeEditText.setError("Field cannot be empty");
            m_dateTime.timeEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // expected pattern HH:MM:SS
        if (!timeInput.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            m_dateTime.timeEditText.setError("invalid time format, expected HH:MM:SS");
            m_dateTime.timeEditText.requestFocus();
            throw new Exception("invalid format");
            // return;
        }

        // parse String to ints
        String[] parts = timeInput.split(":");
        int h, m, s;
        try {
            h = Integer.parseInt(parts[0]);
            m = Integer.parseInt(parts[1]);
            s = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            m_dateTime.timeEditText.setError("Cannot parse value to three ints");
            throw new Exception("parse to 3 ints failed");
        }

        // set data to variable
        m_validatedDataSet.hour = h;
        m_validatedDataSet.minute = m;
        m_validatedDataSet.second = s;
    }

    static private boolean isValidDayForMonth(int year, int month, int day) {
        if (month == 2) { // February
            boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            return day <= (isLeapYear ? 29 : 28);
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30; // April, June, October, November
        } else {
            return day <= 31; // other months
        }
    }

    private void processDateValue() throws Exception {
        String dateInput = m_dateTime.dateEditText.getText().toString().trim();
        Log.i("INITIAL_CONFIGURATION", "dateInput value: " + dateInput);

        if (TextUtils.isEmpty(dateInput)) {
            m_dateTime.dateEditText.setError("Field cannot be empty");
            m_dateTime.dateEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // expected pattern YYY/MM/DD
        if (!dateInput.matches("^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$")) {
            m_dateTime.dateEditText.setError("invalid date format,\n expected YYYY/MM/DD");
            m_dateTime.dateEditText.requestFocus();
            throw new Exception("invalid format");
            // return;
        }

        // parse String to ints
        String[] parts = dateInput.split("/");
        int year, month, day;
        try {
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            m_dateTime.dateEditText.setError("Cannot parse value to three ints");
            throw new Exception("parse to 3 ints failed");
        }

        // check if February does not have 31 days
        if (!InitAppActivity.isValidDayForMonth(year, month, day)) {
            m_dateTime.dateEditText.setError("invalid day for that month and year");
            throw new Exception("invalid day for that month");
            // return;
        }

        // set data to variable
        m_validatedDataSet.year = year;
        m_validatedDataSet.month = month;
        m_validatedDataSet.day = day;
    }

    private void closeInitActivity(){
        // add data to return

        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("FLOAT_VALUE_1", floatValue1);
//        intent.putExtra("FLOAT_VALUE_2", floatValue2);
//        intent.putExtra("DATA_CLASS_1", data1);
//        intent.putExtra("DATA_CLASS_2", data2);

//        startActivity(intent);
        finish();
    }
}