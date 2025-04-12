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

import com.androsiukcezary.fuelcalculator.data.EndTripRecordModel;
import com.androsiukcezary.fuelcalculator.data.FirstRecordModel;
import com.androsiukcezary.fuelcalculator.data.FuelRecordModel;
import com.androsiukcezary.fuelcalculator.data.FuelRecordType;
import com.androsiukcezary.fuelcalculator.data.PaymentRecordModel;
import com.androsiukcezary.fuelcalculator.data.RefuelingRecordModel;
import com.androsiukcezary.fuelcalculator.data.StartTripRecordModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final public static String m_dataFilePath = "./data.txt";

    InitDataSet m_initDataSet;

    FuelRecordRecyclerViewAdapter recyclerAdapter;
    ArrayList<FuelRecordModel> fuelData = new ArrayList<>();

    ActivityResultLauncher<Intent> m_settingsActivityResultLauncher;
    ImageButton m_settingsButton;

    ActivityResultLauncher<Intent> m_addNewStartTripActivityResultLauncher;
    ActivityResultLauncher<Intent> m_addNewEndTripActivityResultLauncher;
    ActivityResultLauncher<Intent> m_addNewRefuelingActivityResultLauncher;
    ActivityResultLauncher<Intent> m_addNewPayemntActivityResultLauncher;

    boolean m_tripStarted = false;

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
        this.createAddNewStartTripActivityLoader();
        this.createAddNewEndTripActivityLoader();
        this.createAddNewRefuelingActivityLoader();
        this.createAddNewPaymentActivityLoader();

        this.initRecyclerView();

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

        fuelData.add(new FirstRecordModel(
                m_initDataSet.validatedInitialPLNValue,
                m_initDataSet.validatedCurrentFuel,
                m_initDataSet.validatedCurrentFuelPrice,
                m_initDataSet.timeDateDataSet
        ));
        recyclerAdapter.notifyItemInserted(fuelData.size() - 1);

        ///  readDataFile not required all data are in memory
        this.useDataToStartAlreadyInitializedApplication();
    }


    ///
    /// MAIN ACTIVITY
    ///
    private void readDataFile(){ ///  called before useDataToStartAlreadyInitializedApplication()
        Log.i("MAIN_ACTIVITY_LOGS", "readDataFile");

    }

    private void initRecyclerView(){
        RecyclerView mainRecyclerView = findViewById(R.id.mainRecyclerView);

        recyclerAdapter =
                new FuelRecordRecyclerViewAdapter(this, fuelData);
        mainRecyclerView.setAdapter(recyclerAdapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager((((((((this)))))))));
    }

    private void useDataToStartAlreadyInitializedApplication(){
        Log.i("MAIN_ACTIVITY_LOGS", "useDataToStartAlreadyInitializedApplication");



        ///  temporary create data to display
//        int size = 50;
//        double[] fromFuels = new double[size];
//        for(int i=0; i<size; i++)
//            fromFuels[size-i-1] = i* 3.0;
//        double[] toFuels = new double[size];
//        for(int i=0; i<size; i++)
//            toFuels[size-i-1] = (i+1)* 3.0;
//
//        for(int i=0; i<size; i++)
//        {
//            m_fuelRecordModels.add( new FuelRecordModel(fromFuels[i], toFuels[i]) );
//        }

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

        ///  Start Trip Recordd Button
        Button startTripButton = (Button) newRecordDialog.findViewById(R.id.btnDialogStartTrip);
        startTripButton.setEnabled(!m_tripStarted);
        startTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
                openAddNewStartTripRecordActivity();
            }
        });
        ///  End Trip Recordd Button
        Button endTripButton = (Button) newRecordDialog.findViewById(R.id.btnDialogEndTrip);
        endTripButton.setEnabled(m_tripStarted);
        endTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
                openAddNewEndTripRecordActivity();
            }
        });
        ///  New Refueling Record Button
        Button refuelingButton = (Button) newRecordDialog.findViewById(R.id.btnDialogRefueling);
        refuelingButton.setEnabled(!m_tripStarted);
        refuelingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
                openAddNewRefuelingRecordActivity();
            }
        });
        ///  New Payment Record Button
        Button paymentButton = (Button) newRecordDialog.findViewById(R.id.btnDialogPayment);
        paymentButton.setEnabled(!m_tripStarted);
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
    private void createAddNewStartTripActivityLoader(){
        Log.i("MAIN_ACTIVITY_LOGS", "createAddNewStartTripActivityLoader");
        m_addNewStartTripActivityResultLauncher = registerForActivityResult(
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
                            double currentFuel = data.getDoubleExtra("CURRENT_FUEL", -1.0);
                            TimeDateDataSet timeDataDataSet = (TimeDateDataSet) data.getSerializableExtra("TIME_DATA_DATA_SET");

                            addNewStartTripRecord(currentFuel, timeDataDataSet);
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
    private void createAddNewEndTripActivityLoader(){
        Log.i("MAIN_ACTIVITY_LOGS", "createAddNewEndTripActivityLoader");
        m_addNewEndTripActivityResultLauncher = registerForActivityResult(
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
                            double currentFuel = data.getDoubleExtra("CURRENT_FUEL", -1.0);
                            TimeDateDataSet timeDataDataSet = (TimeDateDataSet) data.getSerializableExtra("TIME_DATA_DATA_SET");

                            addNewEndTripRecord(currentFuel, timeDataDataSet);
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
        Log.i("MAIN_ACTIVITY_LOGS", "createAddNewRefuelingActivityLoader");
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
                            double refueledQuantity = data.getDoubleExtra("REFUELED_QUANTITY", -1.0);
                            double fuelPrice = data.getDoubleExtra("FUEL_PRICE", -1.0);
                            boolean otherCarUserPays = data.getBooleanExtra("OTHER_CAR_USER_PAYS", false);
                            TimeDateDataSet timeDataDataSet = (TimeDateDataSet) data.getSerializableExtra("TIME_DATA_DATA_SET");

                            addNewRefuelingRecord(refueledQuantity, fuelPrice, otherCarUserPays, timeDataDataSet);
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
        Log.i("MAIN_ACTIVITY_LOGS", "createAddNewPaymentActivityLoader");
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
                            double moneyPaid = data.getDoubleExtra("MONEY_PAID", -1.0);
                            TimeDateDataSet timeDataDataSet = (TimeDateDataSet) data.getSerializableExtra("TIME_DATA_DATA_SET");

                            addNewPaymentRecord(moneyPaid, timeDataDataSet);
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

    private void openAddNewStartTripRecordActivity(){
        Log.i("MAIN_ACTIVITY_LOGS", "openAddStartTripRecordActivity");

        Intent intent = new Intent(this, AddNewStartTripRecord.class);
        m_addNewStartTripActivityResultLauncher.launch(intent);
    }

    private void openAddNewEndTripRecordActivity(){
        Log.i("MAIN_ACTIVITY_LOGS", "openAddEndTripRecordActivity");

        Intent intent = new Intent(this, AddNewEndTripRecord.class);
        m_addNewEndTripActivityResultLauncher.launch(intent);
    }

    private void openAddNewRefuelingRecordActivity(){
        Log.i("MAIN_ACTIVITY_LOGS", "openAddNewRefuelingRecord");

        Intent intent = new Intent(this, AddNewRefuelingRecord.class);
        m_addNewRefuelingActivityResultLauncher.launch(intent);
    }

    private void openAddNewPaymentRecordActivity(){
        Log.i("MAIN_ACTIVITY_LOGS", "openAddNewPaymentRecord");

        Intent intent = new Intent(this, AddNewPaymentRecord.class);
        m_addNewPayemntActivityResultLauncher.launch(intent);
    }

    private void addNewStartTripRecord(double currentFuel, TimeDateDataSet timeDateDataSet){
        Log.i("MAIN_ACTIVITY_LOGS", "addNewStartTripRecord");
        Log.i("MAIN_ACTIVITY_LOGS", "currentFuel: " + Double.toString(currentFuel) +", " + timeDateDataSet);

        fuelData.add(new StartTripRecordModel(
                currentFuel, timeDateDataSet
        ));
        recyclerAdapter.notifyItemInserted(fuelData.size() - 1);

        this.m_tripStarted = true;
    }

    private void addNewEndTripRecord(double currentFuel, TimeDateDataSet timeDateDataSet){
        Log.i("MAIN_ACTIVITY_LOGS", "addNewEndTripRecord");
        Log.i("MAIN_ACTIVITY_LOGS", "currentFuel: " + Double.toString(currentFuel) +", " + timeDateDataSet);

        fuelData.add(new EndTripRecordModel(
                currentFuel, timeDateDataSet
        ));
        recyclerAdapter.notifyItemInserted(fuelData.size() - 1);

        this.m_tripStarted = false;
    }

    private void addNewRefuelingRecord(
            double refueledQuantity, double fuelPrice,
            boolean otherCarUserPays, TimeDateDataSet timeDataDataSet)
    {
        Log.i("MAIN_ACTIVITY_LOGS", "addNewRefuelingRecord");
        Log.i("MAIN_ACTIVITY_LOGS", "refueledQuantity: " + Double.toString(refueledQuantity)
                + ", fuelPrice: " + Double.toString(fuelPrice)
                + ", otherCarUserPays: " + Boolean.toString(otherCarUserPays)
                + ", " + timeDataDataSet
        );

        fuelData.add(new RefuelingRecordModel(
                refueledQuantity, fuelPrice, otherCarUserPays, timeDataDataSet
        ));
        recyclerAdapter.notifyItemInserted(fuelData.size() - 1);

    }

    private void addNewPaymentRecord(double moneyPaid, TimeDateDataSet timeDataDataSet){
        Log.i("MAIN_ACTIVITY_LOGS", "addNewPaymentRecord");
        Log.i("MAIN_ACTIVITY_LOGS", "moneyPaid: " + Double.toString(moneyPaid) + ", " + timeDataDataSet);

        fuelData.add(new PaymentRecordModel(
                moneyPaid, timeDataDataSet
        ));
        recyclerAdapter.notifyItemInserted(fuelData.size() - 1);
    }
}