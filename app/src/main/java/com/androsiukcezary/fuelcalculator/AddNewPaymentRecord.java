package com.androsiukcezary.fuelcalculator;

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

public class AddNewPaymentRecord extends AppCompatActivity {

    EditText m_currentFuelEditText;
    Button m_saveNewPaymentButton;
    DateTimeStruct m_dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        m_currentFuelEditText = (EditText) findViewById(R.id.currentFuelEditText);
        m_saveNewPaymentButton = (Button) findViewById(R.id.saveNewPaymentButton);

        ///  create date time structure and assign views to them
        m_dateTime = new DateTimeStruct(
                this,
                findViewById(R.id.timeEditText),
                findViewById(R.id.timeSelectButton),
                findViewById(R.id.dateEditText),
                findViewById(R.id.dateSelectButton)
        );

    }

    private void closeActivity(){
        Log.i("ADD_NEW_PAYMENT_RECORD_ACTIVITY_LOGS", "closeActivity");
        setResult(RESULT_CANCELED);
        finish();
    }
}