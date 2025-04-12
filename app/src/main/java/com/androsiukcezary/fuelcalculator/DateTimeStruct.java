package com.androsiukcezary.fuelcalculator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeStruct implements Serializable {

    private Context context;

    public EditText timeEditText;
    public ImageButton timeSelectButton;

    public EditText dateEditText;
    public ImageButton dateSelectButton;


    static final String timePattern = "HH:mm:ss";
    static final String datePattern = "yyyy/MM/dd";

    public DateTimeStruct(Context context, View timeEditText, View timeSelectButton, View dateEditText, View dateSelectButton) {
        Log.i("DATE_TIME_STRUCT_ACTIVITY_LOGS", "DateTimeStruct");
        this.context = context;
        this.timeEditText = (EditText) timeEditText;
        this.timeSelectButton = (ImageButton) timeSelectButton;
        this.dateEditText = (EditText) dateEditText;
        this.dateSelectButton = (ImageButton) dateSelectButton;

        ///  assign initial time
        SimpleDateFormat timeFormat = new SimpleDateFormat(
                DateTimeStruct.timePattern, Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        this.timeEditText.setText(currentTime);
        ///  connect onClick function with button
        this.timeSelectButton.setOnClickListener(v -> openTimeSelector());

        ///  assign initial date
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DateTimeStruct.datePattern, Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        this.dateEditText.setText(currentDate);
        ///  connect onClick function with button
        this.dateSelectButton.setOnClickListener(v -> openDateSelector());

    }

    ///  Open while changing the time
    private void openTimeSelector(){
        ///  read necessary data from calendar
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        ///  optional addition by reading currently set time and only if fails, then use current
//        int hour = currentHour;
//        int minute = currentMinute;

        new TimePickerDialog(context, (view, initHour, initMinute) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d:00", initHour, initMinute);
            timeEditText.setText(time);
        }, currentHour, currentMinute, true).show();
    }

    ///  Open while changing the date
    private void openDateSelector(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfM = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(),
                            "%04d/%02d/%02d", year, month + 1, dayOfMonth);
                    dateEditText.setText(selectedDate);
                }, currentYear, currentMonth, currentDayOfM
        );
        datePickerDialog.show();
    }



    public void processTimeValue(TimeDateDataSet dataSet) throws Exception {
        Log.i("DATE_TIME_STRUCT_ACTIVITY_LOGS", "processTimeValue");
        String timeInput = timeEditText.getText().toString().trim();
        Log.i("DATE_TIME_STRUCT_ACTIVITY_LOGS", "timeInput value: " + timeInput);

        if (TextUtils.isEmpty(timeInput)) {
            timeEditText.setError("Field cannot be empty");
            timeEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // expected pattern HH:MM:SS
        if (!timeInput.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$")) {
            timeEditText.setError("invalid time format, expected HH:MM:SS");
            timeEditText.requestFocus();
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
            timeEditText.setError("Cannot parse value to three ints");
            throw new Exception("parse to 3 ints failed");
        }

        // set data to variable
        dataSet.hour = h;
        dataSet.minute = m;
        dataSet.second = s;
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

    public void processDateValue(TimeDateDataSet dataSet) throws Exception {
        Log.i("DATE_TIME_STRUCT_ACTIVITY_LOGS", "processDateValue");
        String dateInput = dateEditText.getText().toString().trim();
        Log.i("DATE_TIME_STRUCT_ACTIVITY_LOGS", "dateInput value: " + dateInput);

        if (TextUtils.isEmpty(dateInput)) {
            dateEditText.setError("Field cannot be empty");
            dateEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // expected pattern YYY/MM/DD
        if (!dateInput.matches("^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$")) {
            dateEditText.setError("invalid date format,\n expected YYYY/MM/DD");
            dateEditText.requestFocus();
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
            dateEditText.setError("Cannot parse value to three ints");
            throw new Exception("parse to 3 ints failed");
        }

        // check if February does not have 31 days
        if (!this.isValidDayForMonth(year, month, day)) {
            dateEditText.setError("invalid day for that month and year");
            throw new Exception("invalid day for that month");
            // return;
        }

        // set data to variable
        dataSet.year = year;
        dataSet.month = month;
        dataSet.day = day;
    }

}
