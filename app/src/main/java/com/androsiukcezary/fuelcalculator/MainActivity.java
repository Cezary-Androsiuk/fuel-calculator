package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final public static String m_dataFilePath = "./data.txt";

    InitDataSet m_initDataSet;

    ArrayList<FuelRecordModel> m_fuelRecordModels = new ArrayList<>();

    ActivityResultLauncher<Intent> m_settingsActivityResultLauncher;
    ImageButton m_settingsButton;

    ActivityResultLauncher<Intent> m_addNewTripActivityResultLauncher;
    ActivityResultLauncher<Intent> m_addNewRefuelingActivityResultLauncher;
    ActivityResultLauncher<Intent> m_addNewPayemntActivityResultLauncher;

    /// NOT WORKS
//    // prevents restarting while device changed position form vertical to horizontal
//    boolean m_activityInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MAIN_ACTIVITY_LOGS", "onCreate");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        // if I only can handle double start InitAppActivity I will deal with something like "good user experience" XD
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.createSettingsActivityLoader();
        this.createAddNewTripActivityLoader();
        this.createAddNewRefuelingActivityLoader();
        this.createAddNewPaymentActivityLoader();

        m_settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        m_settingsButton.setOnClickListener(v -> this.openSettings());

        /// NOT WORKS
//        if(!m_activityInitialized)
//        {
//            m_activityInitialized = true;
//        }

        {
            if(!this.isDataFileExit())
            {
                startInitAppActivity();
            }
            else
            {
                readDataFile();
                useDataToStartAlreadyInitializedApplication();
            }
        }
    }

    @Override
    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        finishAffinity(); // Closes all activities and ends the application process
        System.exit(0); // exit to ensure that there will be exit if something fails
    }


    ///
    /// SETTINGS ACTIVITY
    ///
    private void createSettingsActivityLoader() {
         m_settingsActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == SettingsActivity.RESULT_ERASE_APP_MEMORY)
                        {
                            eraseAppMemory();
                        }
                        else if(o.getResultCode() == RESULT_OK)
                        {
                            ///  do nothing
                        }
                        else
                        {
                            ///  do nothing
                        }
                    }
                });

    }

    private void openSettings(){
        Log.i("MAIN_ACTIVITY_LOGS", "openSettings");

        Intent intent = new Intent(this, SettingsActivity.class);
        m_settingsActivityResultLauncher.launch(intent);
    }

    private void eraseAppMemory(){
        ///  clear list of fuel records

        ///  delete data file

        ///  restart application to commit changes
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finishAffinity();
        }, 300); // 300ms delay helps in avoiding errors

    }

    ///
    /// INIT APP ACTIVITY
    ///

    private boolean isDataFileExit(){
        // check if data file exit

        return false; // temporary
    }

    private void startInitAppActivity() {
        Log.i("MAIN_ACTIVITY_LOGS", "startInitAppActivity");
        ActivityResultLauncher<Intent> initAppActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == RESULT_OK) {
                            Intent data = o.getData();
                            if(data == null)
                            {
                                Log.i("MAIN_ACTIVITY_LOGS", "data is null");

                                /// IDK WHAT TO DO HERE, SEAMS LIKE A DEAD END WHERE APP CRASH
                                finishAffinity(); // Closes all activities and ends the application process
                                System.exit(0); // exit to ensure that there will be exit if something fails
                                return;
                            }

                            m_initDataSet = (InitDataSet) data.getSerializableExtra("INIT_DATA_SET");
                            onInitAppFinished();
                        }
                        else
                        {
                            ///  do nothing
                        }
                    }
                });

        Intent intent = new Intent(this, InitAppActivity.class);
        initAppActivityResultLauncher.launch(intent);
    }

    private void onInitAppFinished(){
        Log.i("MAIN_ACTIVITY_LOGS", "onInitAppFinished");
        Log.i("MAIN_ACTIVITY_LOGS", "m_initDataSet: " + m_initDataSet);
        // transform initDataSet to something useful

        ///  readDataFile not required all data are in memory
        this.useDataToStartAlreadyInitializedApplication();
    }


    ///
    /// MAIN ACTIVITY
    ///
    private void readDataFile(){ ///  called before useDataToStartAlreadyInitializedApplication()
        Log.i("MAIN_ACTIVITY_LOGS", "readDataFile");

    }

    private void useDataToStartAlreadyInitializedApplication(){
        Log.i("MAIN_ACTIVITY_LOGS", "useDataToStartAlreadyInitializedApplication");



        ///  temporary create data to display
        int size = 50;
        double[] fromFuels = new double[size];
        for(int i=0; i<size; i++)
            fromFuels[size-i-1] = i* 3.0;
        double[] toFuels = new double[size];
        for(int i=0; i<size; i++)
            toFuels[size-i-1] = (i+1)* 3.0;

        for(int i=0; i<size; i++)
        {
            m_fuelRecordModels.add( new FuelRecordModel(fromFuels[i], toFuels[i]) );
        }

        RecyclerView mainRecyclerView = findViewById(R.id.mainRecyclerView);

        FuelRecordRecyclerViewAdapter recyclerAdapter =
                new FuelRecordRecyclerViewAdapter(this, m_fuelRecordModels);
        mainRecyclerView.setAdapter(recyclerAdapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager((((((((this)))))))));
    }

    public void addNewRecord(){
        ///  Show New Record Dialog
        Dialog newRecordDialog = new Dialog(this);
        newRecordDialog.setContentView(R.layout.add_new_record_dialog_box);
        newRecordDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newRecordDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_box_background));
        newRecordDialog.setCancelable(true);
        newRecordDialog.show();

        ///  New Trip Recordd Button
        Button tripButton = (Button) newRecordDialog.findViewById(R.id.btnDialogTrip);
        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
                openAddNewTripRecordActivity();
            }
        });
        ///  New Refueling Record Button
        Button refuelingButton = (Button) newRecordDialog.findViewById(R.id.btnDialogRefueling);
        refuelingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
                openAddNewRefuelingRecordActivity();
            }
        });
        ///  New Payment Record Button
        Button paymentButton = (Button) newRecordDialog.findViewById(R.id.btnDialogPayment);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
                openAddNewPaymentRecordActivity();
            }
        });
        ///  Cancel Adding New Record Button
        Button cancelButton = (Button) newRecordDialog.findViewById(R.id.btnDialogCancelAddingRecord);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
            }
        });
    }


    ///
    /// New Record Activities
    ///
    private void createAddNewTripActivityLoader(){
        m_addNewTripActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == RESULT_OK)
                        {
                            Intent data = o.getData();
                            if(data == null)
                            {
                                Log.i("MAIN_ACTIVITY_LOGS", "data is null");

                                /// IDK WHAT TO DO HERE, SEAMS LIKE A DEAD END WHERE APP CRASH
                                finishAffinity(); // Closes all activities and ends the application process
                                System.exit(0); // exit to ensure that there will be exit if something fails
                                return;
                            }
                            data.getSerializableExtra("");

                            addNewTripRecord();
                        }
                        else if(o.getResultCode() == RESULT_CANCELED)
                        {
                            ///  do nothing
                        }
                        else
                        {
                            ///  do nothing
                        }
                    }
                });
    }
    private void createAddNewRefuelingActivityLoader(){
        m_addNewRefuelingActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == RESULT_OK)
                        {
                            Intent data = o.getData();
                            if(data == null)
                            {
                                Log.i("MAIN_ACTIVITY_LOGS", "data is null");

                                /// IDK WHAT TO DO HERE, SEAMS LIKE A DEAD END WHERE APP CRASH
                                finishAffinity(); // Closes all activities and ends the application process
                                System.exit(0); // exit to ensure that there will be exit if something fails
                                return;
                            }
                            data.getSerializableExtra("");

                            addNewRefuelingRecord();
                        }
                        else if(o.getResultCode() == RESULT_CANCELED)
                        {
                            ///  do nothing
                        }
                        else
                        {
                            ///  do nothing
                        }
                    }
                });
    }
    private void createAddNewPaymentActivityLoader(){
        m_addNewPayemntActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == RESULT_OK)
                        {
                            Intent data = o.getData();
                            if(data == null)
                            {
                                Log.i("MAIN_ACTIVITY_LOGS", "data is null");

                                /// IDK WHAT TO DO HERE, SEAMS LIKE A DEAD END WHERE APP CRASH
                                finishAffinity(); // Closes all activities and ends the application process
                                System.exit(0); // exit to ensure that there will be exit if something fails
                                return;
                            }
                            data.getSerializableExtra("");

                            addNewPaymentRecord();
                        }
                        else if(o.getResultCode() == RESULT_CANCELED)
                        {
                            ///  do nothing
                        }
                        else
                        {
                            ///  do nothing
                        }
                    }
                });
    }

    private void openAddNewTripRecordActivity(){
        Log.i("MAIN_ACTIVITY_LOGS", "openAddNewTripRecord");

        Intent intent = new Intent(this, AddNewTripRecord.class);
        m_addNewTripActivityResultLauncher.launch(intent);
    }

    private void openAddNewRefuelingRecordActivity(){
        Log.i("MAIN_ACTIVITY_LOGS", "openAddNewRefuelingRecord");

        Intent intent = new Intent(this, AddNewRefuelingRecord.class);
        m_addNewTripActivityResultLauncher.launch(intent);

    }

    private void openAddNewPaymentRecordActivity(){
        Log.i("MAIN_ACTIVITY_LOGS", "openAddNewPaymentRecord");

        Intent intent = new Intent(this, AddNewPaymentRecord.class);
        m_addNewTripActivityResultLauncher.launch(intent);

    }

    private void addNewTripRecord(){

    }

    private void addNewRefuelingRecord(){

    }

    private void addNewPaymentRecord(){

    }
}