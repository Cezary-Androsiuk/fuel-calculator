package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
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
    Dialog m_eraseAppMemoryDialog;
    Button m_eraseAppMemoryCancelButton;
    Button m_eraseAppMemoryConfirmButton;

    @SuppressLint("UseCompatLoadingForDrawables")
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

        ///  Close settings button
        m_closeSettingsButton = (ImageButton) findViewById(R.id.closeSettingsButton);
        m_closeSettingsButton.setOnClickListener(v -> this.closeSettings());


        ///  Erase App Memory Dialog
        m_eraseAppMemoryDialog = new Dialog(this);
        m_eraseAppMemoryDialog.setContentView(R.layout.confirm_erase_application_data_dialog_box);
        m_eraseAppMemoryDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        m_eraseAppMemoryDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_box_background));
        m_eraseAppMemoryDialog.setCancelable(true);

        ///  Erase App Memory Dialog Cancel Button
        m_eraseAppMemoryCancelButton = m_eraseAppMemoryDialog.findViewById(R.id.btnDialogCancel);
        m_eraseAppMemoryCancelButton.setOnClickListener(v -> this.closeEraseConfirmDialog());
        ///  Erase App Memory Dialog Confirm Button
        m_eraseAppMemoryConfirmButton = m_eraseAppMemoryDialog.findViewById(R.id.btnDialogConfirm);
        m_eraseAppMemoryConfirmButton.setOnClickListener(v -> this.closeWithEraseAppMemorySignal());

        ///  Erase App Memory Dialog Open Button
        m_eraseAppMemoryButton = (Button) findViewById(R.id.eraseApplicationMemoryButton);
        m_eraseAppMemoryButton.setOnClickListener(v -> this.openEraseConfirmDialog());
    }

    private void openEraseConfirmDialog(){
        m_eraseAppMemoryDialog.show();
    }

    private void closeEraseConfirmDialog(){
        m_eraseAppMemoryDialog.dismiss();
    }

    private void closeWithEraseAppMemorySignal(){
        m_eraseAppMemoryDialog.dismiss();
        setResult(RESULT_ERASE_APP_MEMORY);
        finish();
    }

    private void closeSettings(){
        setResult(RESULT_OK);
        finish();
    }
}