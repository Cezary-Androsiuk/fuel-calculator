package com.androsiukcezary.fuelcalculator;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddNewRefuelingRecord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    }

    private void closeActivity(){
        Log.i("ADD_NEW_REFUELING_RECORD_ACTIVITY_LOGS", "closeActivity");
        setResult(RESULT_CANCELED);
        finish();
    }
}