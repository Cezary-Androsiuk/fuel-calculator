package com.androsiukcezary.fuelcalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    static public final int RESULT_ERASE_APP_MEMORY = 38554;

    ImageButton m_closeSettingsButton;
    Button m_eraseAppMemoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        m_closeSettingsButton = (ImageButton) findViewById(R.id.closeSettingsButton);
        m_closeSettingsButton.setOnClickListener(v -> this.closeSettings());


        m_eraseAppMemoryButton = (Button) findViewById(R.id.eraseApplicationMemoryButton);
        m_eraseAppMemoryButton.setOnClickListener(v -> this.closeWithEraseAppMemorySignal());
    }

    private void closeWithEraseAppMemorySignal(){
        setResult(RESULT_ERASE_APP_MEMORY);
        finish();
    }

    private void closeSettings(){
        setResult(RESULT_OK);
        finish();
    }
}