package com.androsiukcezary.fuelcalculator;

import android.content.Intent;
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

public class AddNewPaymentRecord extends AppCompatActivity {

    EditText moneyPaidEditText;
    Button saveNewPaymentButton;
    DateTimeStruct dateTime;

    double moneyPaid;
    TimeDateDataSet timeDateDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ADD_NEW_PAYMENT_RECORD_ACTIVITY_LOGS", "onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_payment_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton closeAddingPaymentButton = (ImageButton) findViewById(R.id.closeAddingPaymentButton);
        closeAddingPaymentButton.setOnClickListener(v -> this.closeActivity());

        timeDateDataSet = new TimeDateDataSet();

        moneyPaidEditText = (EditText) findViewById(R.id.moneyPaidEditText);
        saveNewPaymentButton = (Button) findViewById(R.id.saveNewPaymentButton);

        ///  create date time structure and assign views to them
        dateTime = new DateTimeStruct(
                this,
                findViewById(R.id.timeEditText),
                findViewById(R.id.timeSelectButton),
                findViewById(R.id.dateEditText),
                findViewById(R.id.dateSelectButton)
        );

        saveNewPaymentButton.setOnClickListener(v -> onSaveNewPaymentButtonClicked());
    }

    private void onSaveNewPaymentButtonClicked(){
        Log.i("ADD_NEW_PAYMENT_RECORD_ACTIVITY_LOGS", "onSaveNewPaymentButtonClicked");
        try{
            this.processMoneyPaid();
            dateTime.processTimeValue(timeDateDataSet);
            dateTime.processDateValue(timeDateDataSet);

            this.saveActivity();
        } catch (Exception e) {
            Log.i("ADD_NEW_PAYMENT_RECORD_ACTIVITY_LOGS", "onSaveNewPaymentButtonClicked exception: " + e);
        }

    }

    private void processMoneyPaid() throws Exception{
        Log.i("ADD_NEW_PAYMENT_RECORD_ACTIVITY_LOGS", "processMoneyPaid");
        String moneyPaid = moneyPaidEditText.getText().toString().trim();
        Log.i("ADD_NEW_PAYMENT_RECORD_ACTIVITY_LOGS", "moneyPaid value: " + moneyPaid);

        if (TextUtils.isEmpty(moneyPaid)) {
            moneyPaidEditText.setError("Field cannot be empty");
            moneyPaidEditText.requestFocus();
            throw new Exception("empty value");
            // return;
        }

        // parse String to double
        double parsedValue;
        try {
            String normalizedInput = moneyPaid.replace(",", ".");
            parsedValue = Double.parseDouble(normalizedInput);
        } catch (NumberFormatException e) {
            moneyPaidEditText.setError("Cannot parse value to double");
            throw new Exception("parse to double failed");
        }

        // set data to variable
        this.moneyPaid = parsedValue;
    }

    private void saveActivity(){
        Log.i("ADD_NEW_PAYMENT_RECORD_ACTIVITY_LOGS", "saveActivity");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("MONEY_PAID", moneyPaid);
        returnIntent.putExtra("TIME_DATA_DATA_SET", timeDateDataSet); // class requires serializable

        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void closeActivity(){
        Log.i("ADD_NEW_PAYMENT_RECORD_ACTIVITY_LOGS", "closeActivity");
        setResult(RESULT_CANCELED);
        finish();
    }
}