package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    static public final int RESULT_ERASE_APP_MEMORY = 38554;

    static boolean speedUpFormsForDebug = true;
    Switch speedUpFormsForDebugSwitch;

    ImageButton m_closeSettingsButton;
    Button m_eraseAppMemoryButton;
    Dialog m_eraseAppMemoryDialog;
    Button m_eraseAppMemoryCancelButton;
    Button m_eraseAppMemoryConfirmButton;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SETTINGS_ACTIVITY_LOGS", "onCreate");
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

        speedUpFormsForDebugSwitch = findViewById(R.id.speedUpFormsForDebugSwitch);
        speedUpFormsForDebugSwitch.setChecked(SettingsActivity.speedUpFormsForDebug);
        speedUpFormsForDebugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsActivity.speedUpFormsForDebug = isChecked;
            }
        });

        ///  Export Data to Downloads
        Button exportDataButtonToDownloads = (Button) findViewById(R.id.exportRawDataButtonToDownloads);
        exportDataButtonToDownloads.setOnClickListener(v -> this.exportDataToDownloadsDirectory());

        ///  TEST STUFF
//        findViewById(R.id.textStuffLayout).setVisibility(View.INVISIBLE);
        TextView fileExistStatusTextView = (TextView) findViewById(R.id.fileExistStatusTextView);
        File file = new File(getFilesDir(), "data.json");
        if (file.exists()) {
            fileExistStatusTextView.setText("File Exist");
        } else {
            fileExistStatusTextView.setText("File NOT Exist");
        }

        Button printPWDButton = (Button) findViewById(R.id.printPWDButton);
        printPWDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SETTINGS_ACTIVITY_LOGS", "onClick - printPWDButton");
                String appPath = getApplicationContext().getFilesDir().getAbsolutePath();
//                File file = new File(getFilesDir(), "dynamic_data.json");
                Log.i("SETTINGS_ACTIVITY_LOGS", "current path 2: " + getFilesDir());

                ///  printing filed in directory
                File currentDir = getFilesDir(); // Lub inny folder, np. getCacheDir()
                String[] files = currentDir.list();

                Log.d("SETTINGS_ACTIVITY_LOGS", "files for " + currentDir.getAbsolutePath() + " direcory: ");
                if (files != null) {
                    for (String file : files) {
                        Log.d("SETTINGS_ACTIVITY_LOGS", "Plik: " + file);
                    }
                } else {
                    Log.e("Files", "Brak plików lub brak uprawnień");
                }

                ///  save to download folder
//                File destinationFile = new File(
//                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                        "nowa_nazwa.json"
//                );
//                Log.d("SETTINGS_ACTIVITY_LOGS", "download dir " + destinationFile.getAbsolutePath());
//                try {
//                    // 1. Utwórz obiekt JSON
//                    JSONObject data = new JSONObject();
//                    data.put("name", "my name");
//                    data.put("age", "18");
//
//                    // 2. Zapisz do pliku
//                    FileWriter writer = new FileWriter(destinationFile);
//                    writer.write(data.toString());
//                    writer.close();
//
//                    Log.d("SETTINGS_ACTIVITY_LOGS", "Dane zapisane");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.i("SETTINGS_ACTIVITY_LOGS", "onClick - addFileButton: failed saving log file ");
//                }
            }
        });

        Button addFileButton = (Button) findViewById(R.id.addFileButton);
        addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SETTINGS_ACTIVITY_LOGS", "onClick - addFileButton");
//                File file = new File(getFilesDir(), "data.json");
                try {
                    // 1. Utwórz obiekt JSON
                    JSONObject data = new JSONObject();
                    data.put("name", "my name");
                    data.put("age", "18");

                    // 2. Zapisz do pliku
                    FileWriter writer = new FileWriter(new File(getFilesDir(), "data.json"));
                    writer.write(data.toString());
                    writer.close();

                    Log.d("SETTINGS_ACTIVITY_LOGS", "Dane zapisane");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("SETTINGS_ACTIVITY_LOGS", "onClick - addFileButton: failed saving log file ");
                }

                File file = new File(getFilesDir(), "data.json");
                if (file.exists()) {
                    fileExistStatusTextView.setText("File Exist");
                } else {
                    fileExistStatusTextView.setText("File NOT Exist");
                }
            }
        });

        Button deleteFileButton = (Button) findViewById(R.id.deleteFileButton);
        deleteFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SETTINGS_ACTIVITY_LOGS", "onClick - deleteFileButton");
                File file = new File(getFilesDir(), "data.json");
                if (file.delete()) {
                    Log.d("SETTINGS_ACTIVITY_LOGS", "Plik usunięty");
                }
                File file2 = new File(getFilesDir(), "data.json");
                if (file2.exists()) {
                    fileExistStatusTextView.setText("File Exist");
                } else {
                    fileExistStatusTextView.setText("File NOT Exist");
                }
            }
        });
    }

    private void exportDataToDownloadsDirectory(){
        Log.i("SETTINGS_ACTIVITY_LOGS", "exportDataToDownloadsDirectory");
        File source = new File(getFilesDir(), MainActivity.dataFilePath); // Przykładowy plik z internal storage
        File destination = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                MainActivity.dataFilePath
        );

        if(copyFileToDownloads(source, destination))
        {
            Log.i("SETTINGS_ACTIVITY_LOGS", "exporing to downlaods failed");
            Toast.makeText(this, "Data downloaded", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.i("SETTINGS_ACTIVITY_LOGS", "exporing to downlaods failed");
            Toast.makeText(this, "Exporting to downloads failed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean copyFileToDownloads(File sourceFile, File destinationFile) {
        try {
            if (!sourceFile.exists()) {
                Log.e("COPY", "Plik źródłowy nie istnieje");
                return false;
            }

            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.e("COPY", "Pamięć zewnętrzna niedostępna");
                return false;
            }

            // Strumienie kopiowania
            FileInputStream inStream = new FileInputStream(sourceFile);
            FileOutputStream outStream = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            // Zamknij strumienie
            inStream.close();
            outStream.close();

            Log.d("COPY", "Skopiowano do: " + destinationFile.getAbsolutePath());
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openEraseConfirmDialog(){
        Log.i("SETTINGS_ACTIVITY_LOGS", "openEraseConfirmDialog");
        m_eraseAppMemoryDialog.show();
    }

    private void closeEraseConfirmDialog(){
        Log.i("SETTINGS_ACTIVITY_LOGS", "closeEraseConfirmDialog");
        m_eraseAppMemoryDialog.dismiss();
    }

    private void closeWithEraseAppMemorySignal(){
        Log.i("SETTINGS_ACTIVITY_LOGS", "closeWithEraseAppMemorySignal");
        m_eraseAppMemoryDialog.dismiss();
        setResult(RESULT_ERASE_APP_MEMORY);
        finish();
    }

    private void closeSettings(){
        Log.i("SETTINGS_ACTIVITY_LOGS", "closeSettings");
        setResult(RESULT_OK);
        finish();
    }
}