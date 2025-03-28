package com.example.fuelcalculator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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

    public class DateTimeStruct{
        public EditText m_timeEditText;
        public ImageButton m_timeSelectButton;

        public EditText m_dateEditText;
        public ImageButton m_dateSelectButton;
    }

    EditText m_initialPLNValueEditText;
    EditText m_currentFuelAmountEditText;
    DateTimeStruct m_dateTime;
    Button m_saveInitDataButton;

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
        m_currentFuelAmountEditText = (EditText) findViewById(R.id.currentFuelAmountEditText);



        ///  create structure and assign views to them
        m_dateTime = new DateTimeStruct();
        m_dateTime.m_timeEditText = (EditText) findViewById(R.id.timeEditText);
        m_dateTime.m_timeSelectButton = (ImageButton) findViewById(R.id.timeSelectButton);
        m_dateTime.m_dateEditText = (EditText) findViewById(R.id.dateEditText);
        m_dateTime.m_dateSelectButton = (ImageButton) findViewById(R.id.dateSelectButton);

        ///  assign initial time
        SimpleDateFormat timeFormat = new SimpleDateFormat(c_timePattern, Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        m_dateTime.m_timeEditText.setText(currentTime);
        ///  connect onClick function with button
        m_dateTime.m_timeSelectButton.setOnClickListener(v -> this.openTimeSelector());

        ///  assign initial date
        SimpleDateFormat dateFormat = new SimpleDateFormat(c_datePattern, Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        m_dateTime.m_dateEditText.setText(currentDate);
        ///  connect onClick function with button
        m_dateTime.m_dateSelectButton.setOnClickListener(v -> this.openDateSelector());



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
            m_dateTime.m_timeEditText.setText(time);
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
                    m_dateTime.m_dateEditText.setText(selectedDate);
                }, currentYear, currentMonth, currentDayOfM
        );
        datePickerDialog.show();
    }

    ///  Called while user press Save (initial data)
    public void onSaveInitDataButtonClicked(){
        ///  check if data are valid
        if(!this.handleInvalidInitData())
        {
            Log.d("db", "invalid data cannot save initialization");
            return;
        }
    }

    private boolean validateInitialPLNValue(){
        return false;
    }

    private boolean handleInvalidInitData(){
        ///  test initial PLN value
        if(!this.validateInitialPLNValue())
        {
            m_initialPLNValueEditText.setError("some error info");
            m_initialPLNValueEditText.requestFocus();
            return false;
        }

        return true;
    }

    public void closeInitActivity(View v){
        // add data to return
        finish();
    }
}