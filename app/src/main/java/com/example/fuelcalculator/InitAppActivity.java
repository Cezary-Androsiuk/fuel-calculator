package com.example.fuelcalculator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

    public class ValidatedTimeStruct{
        public int hour;
        public int minute;
        public int second;
    }

    public class ValidatedDateStruct{
        public int year;
        public int month;
        public int day;
    }

    public class DateTimeStruct{
        public EditText timeEditText;
        public ImageButton timeSelectButton;
        public ValidatedTimeStruct validatedTime;

        public EditText dateEditText;
        public ImageButton dateSelectButton;
        public ValidatedDateStruct validatedDate;
    }

    EditText m_initialPLNValueEditText;
    double m_validatedInitialPLNValue = 0.0;
    EditText m_currentFuelAmountEditText;
    double m_validatedCurrentFuelAmount = 0.0;
    DateTimeStruct m_dateTime;
    Button m_saveInitDataButton;

    final String c_timePattern = "HH:mm:ss";
    final String c_datePattern = "yyyy/MM/dd";

    boolean m_formValid;

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
        m_currentFuelAmountEditText = (EditText) findViewById(R.id.currentFuelAmountEditText);



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
                    String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                    m_dateTime.dateEditText.setText(selectedDate);
                }, currentYear, currentMonth, currentDayOfM
        );
        datePickerDialog.show();
    }

    ///  Called while user press Save (initial data)
    public void onSaveInitDataButtonClicked(){
        m_formValid = true; /// assert - will be changed if needed

        this.processInitialPLNValue();

//        ///  check if data are valid
//        if(!this.handleInvalidInitData())
//        {
//            Log.d("db", "invalid data cannot save initialization");
//            return;
//        }
    }

    private void processInitialPLNValue(){
        
    }

    private boolean validateInitialPLNValue(){
        ///  Always true due to input validation
//        m_initialPLNValueEditText.setError("invalid initial PLN value");
//        m_initialPLNValueEditText.requestFocus();
        return true;
    }

    private boolean validateCurrentFuelAmount(){
        ///  Always true due to input validation
//        m_currentFuelAmountEditText.setError("invalid fuel amount in tank");
//        m_currentFuelAmountEditText.requestFocus();
        return true;
    }

    private boolean validateTime(){
        String timeInput = m_dateTime.timeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(timeInput)) {
            m_dateTime.timeEditText.setError("Field cannot be empty");
            m_dateTime.timeEditText.requestFocus();
            return false;
        }

        // expected pattern HH:MM:SS
        if (!timeInput.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            m_dateTime.timeEditText.setError("invalid time format, expected HH:MM:SS");
            m_dateTime.timeEditText.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidDayForMonth(int year, int month, int day) {
        if (month == 2) { // February
            boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            return day <= (isLeapYear ? 29 : 28);
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30; // April, June, October, November
        } else {
            return day <= 31; // other months
        }
    }

    private boolean validateDate(){
        String dateInput = m_dateTime.dateEditText.getText().toString().trim();

        if (TextUtils.isEmpty(dateInput)) {
            m_dateTime.dateEditText.setError("Field cannot be empty");
            m_dateTime.dateEditText.requestFocus();
            return false;
        }

        // expected pattern YYY/MM/DD
        if (!dateInput.matches("^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$")) {
            m_dateTime.dateEditText.setError("invalid date format,\n expected YYYY/MM/DD");
            m_dateTime.dateEditText.requestFocus();
            return false;
        }

        String[] parts = dateInput.split("/");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        if (!this.isValidDayForMonth(year, month, day)) {
            m_dateTime.dateEditText.setError("invalid day for that month and year");
            return false;
        }

        return true;
    }

    private boolean handleInvalidInitData(){
        ///  test initial PLN value
        if(!this.validateInitialPLNValue())
        {
            return false;
        }

        ///  test current fuel amount
        if(!this.validateCurrentFuelAmount())
        {
            return false;
        }

        ///  test time value
        if(!this.validateTime())
        {
            return false;
        }

        ///  test date value
        if(!this.validateDate())
        {
            return false;
        }

        return true;
    }
//
//    public void setTimeFromEditText(EditText editText) {
//        String timeInput = editText.getText().toString().trim();
//        String[] parts = timeInput.split(":");
//
//        if (parts.length == 3) {
//            try {
//                m_hour = Integer.parseInt(parts[0]);
//                m_minute = Integer.parseInt(parts[1]);
//                m_seconds = Integer.parseInt(parts[2]);
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        } else {
//            throw new IllegalArgumentException("invalid time conversion  HH:MM:SS");
//        }
//    }
//
//    public boolean parseAndSetDateFromEditText(EditText editText) {
//        String dateInput = editText.getText().toString().trim();
//
//        // 1. Sprawdzenie czy pole nie jest puste
//        if (TextUtils.isEmpty(dateInput)) {
//            editText.setError("Wprowadź datę w formacie RRRR/MM/DD");
//            return false;
//        }
//
//        // 2. Sprawdzenie formatu za pomocą regex
//        if (!Pattern.matches("^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$", dateInput)) {
//            editText.setError("Nieprawidłowy format. Wymagany format: RRRR/MM/DD");
//            return false;
//        }
//
//        // 3. Podział na części i konwersja na liczby
//        String[] parts = dateInput.split("/");
//        try {
//            int year = Integer.parseInt(parts[0]);
//            int month = Integer.parseInt(parts[1]);
//            int day = Integer.parseInt(parts[2]);
//
//            // 4. Sprawdzenie poprawności dnia dla miesiąca
//            if (!isValidDayForMonth(year, month, day)) {
//                editText.setError("Nieprawidłowy dzień dla podanego miesiąca");
//                return false;
//            }
//
//            // 5. Przypisanie wartości do pól klasy
//            m_year = year;
//            m_month = month;
//            m_day = day;
//
//            return true;
//
//        } catch (NumberFormatException e) {
//            editText.setError("Nieprawidłowe wartości liczbowe");
//            return false;
//        }
//    }


    public void closeInitActivity(View v){
        // add data to return
        finish();
    }
}