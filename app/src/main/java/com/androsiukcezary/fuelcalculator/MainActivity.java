package com.androsiukcezary.fuelcalculator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final public static String dataFilePath = "fuelRecordsData.dat";
    private Context context;

    FuelRecordRecyclerViewAdapter recyclerAdapter;
    ArrayList<FuelRecordModel> fuelRecordsModels = new ArrayList<>();

    TextView currentBalanceTextView;
    String currentBalancePrefix;
    final String currentBalancePostfix = "zł";
    double currentBalance = 0.0;

    TextView currentFuelTextView;
    String currentFuelPrefix;
    final String currentFuelPostfix = "L";
    double currentFuel = 0.0;

    TextView currentFuelPriceTextView;
    String currentFuelPricePrefix;
    final String currentFuelPricePostfix = "zł";
    double currentFuelPrice = 0.0;

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

        this.context = this.getApplicationContext();

        this.createSettingsActivityLoader();
        this.createAddNewStartTripActivityLoader();
        this.createAddNewEndTripActivityLoader();
        this.createAddNewRefuelingActivityLoader();
        this.createAddNewPaymentActivityLoader();

        this.initRecyclerView();

        this.currentBalanceTextView = (TextView) findViewById(R.id.currentBalanceTextView);
        this.currentBalancePrefix = String.valueOf(currentBalanceTextView.getText());

        this.currentFuelTextView = (TextView) findViewById(R.id.currentFuelTextView);
        this.currentFuelPrefix = String.valueOf(currentFuelTextView.getText());

        this.currentFuelPriceTextView = (TextView) findViewById(R.id.currentFuelPriceTextView);
        this.currentFuelPricePrefix = String.valueOf(currentFuelPriceTextView.getText());

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
                loadFuelRecordsData();
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
        this.fuelRecordsModels.clear();

        ///  delete data file
        File file = new File(getFilesDir(), MainActivity.dataFilePath);
        if (file.delete())
        {
            Log.i("MAIN_ACTIVITY_LOGS", "data file deleted");
        }
        else
        {
            Log.i("MAIN_ACTIVITY_LOGS", "deleting data file failed");
        }

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
        Log.i("MAIN_ACTIVITY_LOGS", "isDataFileExit");
        // check if data file exit
        File file = new File(context.getFilesDir(), MainActivity.dataFilePath);
        return file.exists();
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

                            double initialPLNValue = data.getDoubleExtra("INITIAL_PLN_VALUE", -1.0);
                            double currentFuel = data.getDoubleExtra("CURRENT_FUEL", -1.0);
                            double currentFuelPrice = data.getDoubleExtra("CURRENT_FUEL_PRICE", -1.0);
                            TimeDateDataSet timeDataDataSet = (TimeDateDataSet) data.getSerializableExtra("TIME_DATA_DATA_SET");

                            onInitAppFinished(initialPLNValue, currentFuel, currentFuelPrice, timeDataDataSet);
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

    private void onInitAppFinished(
            double initialPLNValue, double currentFuel,
            double currentFuelPrice,  TimeDateDataSet timeDataDataSet)
    {
        Log.i("MAIN_ACTIVITY_LOGS", "onInitAppFinished");
        Log.i("MAIN_ACTIVITY_LOGS", "initialPLNValue: " + Double.toString(initialPLNValue)
                + ", currentFuel: " + Double.toString(currentFuel)
                + ", currentFuelPrice: " + Double.toString(currentFuelPrice)
                + ", " + timeDataDataSet
        );
        // transform initDataSet to something useful

        this.addFuelRecord(new FirstRecordModel(
                initialPLNValue, currentFuel, currentFuelPrice, timeDataDataSet
        ));

        ///  readDataFile not required all data are in memory
        this.useDataToStartAlreadyInitializedApplication();
    }


    ///
    /// MAIN ACTIVITY
    ///

    private void initRecyclerView(){
        RecyclerView mainRecyclerView = findViewById(R.id.mainRecyclerView);

        recyclerAdapter =
                new FuelRecordRecyclerViewAdapter(this, fuelRecordsModels);
        mainRecyclerView.setAdapter(recyclerAdapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager((((((((this)))))))));
    }

    private void useDataToStartAlreadyInitializedApplication(){
        Log.i("MAIN_ACTIVITY_LOGS", "useDataToStartAlreadyInitializedApplication");

        for(int i=0; i<this.fuelRecordsModels.size(); i++)
            Log.d("MAIN_ACTIVITY_LOGS", "useDataToStartAlreadyInitializedApplication" + fuelRecordsModels.get(i).toString());

        this.updateCurrentBalanceAndFuel();
    }

    private void updateCurrentBalanceAndFuel(){

        this.computeCurrentBalanceAndFuel();
        this.updateCurrentBalanceText();
        this.updateCurrentFuelText();
        this.updateCurrentFuelPriceText();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateCurrentBalanceText(){

        this.currentBalanceTextView.setText(
                this.currentBalancePrefix +
                        String.format("%.2f", currentBalance) +
                        this. currentBalancePostfix);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateCurrentFuelText(){

        this.currentFuelTextView.setText(
                this.currentFuelPrefix +
                        String.format("%.2f", currentFuel) +
                        this. currentFuelPostfix);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateCurrentFuelPriceText(){

        this.currentFuelPriceTextView.setText(
                this.currentFuelPricePrefix +
                        String.format("%.2f", currentFuelPrice) +
                        this. currentFuelPricePostfix);
    }


    private void computeCurrentBalanceAndFuel(){
        Log.i("MAIN_ACTIVITY_LOGS", "computeCurrentBalance");
        double tmpBalance = 0.0;
        double tmpFuel = 0.0;
        double tmpFuelPrice = 0.0;

        int arraySize = this.fuelRecordsModels.size();
        try
        {
            ///  List should contain at first position FirstRecordModel
            Log.d("MAIN_ACTIVITY_LOGS", "FirstRecordModel");
            FuelRecordModel model = this.fuelRecordsModels.get(arraySize -1);
            FirstRecordModel firstRecordModel = (FirstRecordModel) model;
            firstRecordModel.setCurrentFuel(60);
            tmpBalance = firstRecordModel.getInitialPLNValue();
            tmpFuel = firstRecordModel.getCurrentFuel();
            tmpFuelPrice = firstRecordModel.getCurrentFuelPrice();

            StartTripRecordModel lastStartTripRecordModel = null;
            EndTripRecordModel lastEndTripRecordModel = null;
            Log.d("MAIN_ACTIVITY_LOGS", "balance: " + tmpBalance + ", fuel: " + tmpFuel + ", fuelPrice: " + tmpFuelPrice);

            for(int i=arraySize-2; i>=0; i--)
            {
                model = this.fuelRecordsModels.get(i);
                if(model instanceof StartTripRecordModel)
                {
                    Log.d("MAIN_ACTIVITY_LOGS", "StartTripRecordModel");
                    StartTripRecordModel startTripRecordModel = (StartTripRecordModel) model;
                    lastStartTripRecordModel = startTripRecordModel;

                    if(lastEndTripRecordModel == null)
                    {
                        Log.d("MAIN_ACTIVITY_LOGS", "lastEndTripRecordModel is null - first record");
                        double startFuel = startTripRecordModel.getCurrentFuel();
                        double fuelUsedByOtherUser = 0.0 - startFuel;
                        Log.w("MAIN_ACTIVITY_LOGS", "not finished, hard typed number");
                        Log.d("MAIN_ACTIVITY_LOGS", "fuelUsedByOtherUser: " + fuelUsedByOtherUser);
                        if(fuelUsedByOtherUser >= 0)
                        {
                            tmpBalance += fuelUsedByOtherUser * tmpFuelPrice;
                            tmpFuel -= fuelUsedByOtherUser;
                        }
                        else {
                            Log.d("MAIN_ACTIVITY_LOGS", "used fuel < 0 - skipped");
                        }

                    }
                    else
                    {
                        double newStartFuel = startTripRecordModel.getCurrentFuel();
                        double lastEndFuel = lastEndTripRecordModel.getCurrentFuel();
                        double fuelUsedByOtherUser = newStartFuel - lastEndFuel;
                        Log.d("MAIN_ACTIVITY_LOGS", "fuelUsedByOtherUser: " + fuelUsedByOtherUser);
                        if(fuelUsedByOtherUser >= 0)
                        {
                            tmpBalance += fuelUsedByOtherUser * tmpFuelPrice;
                            tmpFuel -= fuelUsedByOtherUser;
                        }
                        else {
                            Log.d("MAIN_ACTIVITY_LOGS", "used fuel < 0 - skipped");
                        }
                    }

                    Log.d("MAIN_ACTIVITY_LOGS", "balance: " + tmpBalance + ", fuel: " + tmpFuel + ", fuelPrice: " + tmpFuelPrice);
                }
                else if(model instanceof EndTripRecordModel)
                {
                    Log.d("MAIN_ACTIVITY_LOGS", "EndTripRecordModel");
                    EndTripRecordModel endTripRecordModel = (EndTripRecordModel) model;
                    lastEndTripRecordModel = endTripRecordModel;

                    if(lastStartTripRecordModel == null)
                    {
                        Log.e("MAIN_ACTIVITY_LOGS", "lastStartTripRecordModel is null");
                        continue;
                    }

                    double startFuel = lastStartTripRecordModel.getCurrentFuel();
                    double endFuel = endTripRecordModel.getCurrentFuel();
                    double fuelUsedByMe = endFuel - startFuel;
                    if(fuelUsedByMe >= 0)
                    {
                        tmpFuel -= fuelUsedByMe;
                    }
                    else {
                        Log.d("MAIN_ACTIVITY_LOGS", "used fuel < 0 - skipped");
                    }
                    Log.d("MAIN_ACTIVITY_LOGS", "balance: " + tmpBalance + ", fuel: " + tmpFuel + ", fuelPrice: " + tmpFuelPrice);
                }
                else if(model instanceof RefuelingRecordModel)
                {
                    Log.d("MAIN_ACTIVITY_LOGS", "RefuelingRecordModel");
                    RefuelingRecordModel refuelingRecordModel = (RefuelingRecordModel) model;
                    double refuelQuantity = refuelingRecordModel.getRefueledQuantity();
                    double refuelPrice = refuelingRecordModel.getFuelPrice();
                    boolean otherCarUserPays = refuelingRecordModel.isOtherCarUserPays();

                    ///  update fuel price (in tank)
                    double sumFuel = tmpFuel + refuelQuantity;
                    double tmpFuelRatio = tmpFuel/sumFuel;
                    double refuelQuantityRatio = refuelQuantity/sumFuel;
                    tmpFuelPrice = refuelPrice * refuelQuantityRatio + tmpFuelPrice * tmpFuelRatio;

                    ///  update fuel amount (in tank)
                    tmpFuel += refuelQuantity;

                    ///  update balance
                    if(otherCarUserPays)
                    {
                        tmpBalance -= refuelQuantity * refuelPrice;
                    }
                    Log.d("MAIN_ACTIVITY_LOGS", "balance: " + tmpBalance + ", fuel: " + tmpFuel + ", fuelPrice: " + tmpFuelPrice);

                }
                else if(model instanceof PaymentRecordModel)
                {
                    Log.d("MAIN_ACTIVITY_LOGS", "PaymentRecordModel");
                    PaymentRecordModel paymentRecordModel = (PaymentRecordModel) model;
                    tmpBalance -= paymentRecordModel.getMoneyPaid();
                    Log.d("MAIN_ACTIVITY_LOGS", "balance: " + tmpBalance + ", fuel: " + tmpFuel + ", fuelPrice: " + tmpFuelPrice);
                }
            }
        }
        catch (Exception e)
        {
            Log.e("MAIN_ACTIVITY_LOGS", "computeCurrentBalance failed: " + e);
        }


        currentBalance = tmpBalance;
        currentFuel = tmpFuel;
        currentFuelPrice = tmpFuelPrice;
    }

    public void askIfAddNewRecord(){
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

    public void askIfDeleteRecord(int position){
        ///  Show New Record Dialog
        Dialog newRecordDialog = new Dialog(this);
        newRecordDialog.setContentView(R.layout.confirm_delete_newest_fuel_record_dialog_box);
        newRecordDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newRecordDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_box_background));
        newRecordDialog.setCancelable(true);
        newRecordDialog.show();

        ///  Cancel
        Button cancelButton = (Button) newRecordDialog.findViewById(R.id.btnDialogCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
            }
        });

        ///  Confirm deleting newest record
        Button confirmButton = (Button) newRecordDialog.findViewById(R.id.btnDialogConfirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordDialog.dismiss();
                deleteFuelRecord(position);
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

        this.addFuelRecord(new StartTripRecordModel(
                currentFuel, timeDateDataSet
        ));

        this.m_tripStarted = true;
    }

    private void addNewEndTripRecord(double currentFuel, TimeDateDataSet timeDateDataSet){
        Log.i("MAIN_ACTIVITY_LOGS", "addNewEndTripRecord");
        Log.i("MAIN_ACTIVITY_LOGS", "currentFuel: " + Double.toString(currentFuel) +", " + timeDateDataSet);

        this.addFuelRecord(new EndTripRecordModel(
                currentFuel, timeDateDataSet
        ));

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

        this.addFuelRecord(new RefuelingRecordModel(
                refueledQuantity, fuelPrice, otherCarUserPays, timeDataDataSet
        ));

    }

    private void addNewPaymentRecord(double moneyPaid, TimeDateDataSet timeDataDataSet){
        Log.i("MAIN_ACTIVITY_LOGS", "addNewPaymentRecord");
        Log.i("MAIN_ACTIVITY_LOGS", "moneyPaid: " + Double.toString(moneyPaid) + ", " + timeDataDataSet);

        this.addFuelRecord(new PaymentRecordModel(
                moneyPaid, timeDataDataSet
        ));
    }




    ///
    ///  Following methods can be moved to FuelRecordsData class (if I want to extend that)
    ///

    private void addFuelRecord(FuelRecordModel model){
        Log.i("MAIN_ACTIVITY_LOGS", "addFuelRecord");
        try
        {
            fuelRecordsModels.add(0, model);
            recyclerAdapter.notifyItemInserted(0);
            this.updateCurrentBalanceAndFuel();

            this.saveFuelRecordsData();
        }
        catch (Exception e)
        {
            Log.e("MAIN_ACTIVITY_LOGS", "adding fuel record failed: " + e);
        }
    }

    private void deleteFuelRecord(int position){
        Log.i("MAIN_ACTIVITY_LOGS", "deleteFuelRecord");
        try
        {
            fuelRecordsModels.remove(position);
            recyclerAdapter.notifyItemRemoved(position);
            this.updateCurrentBalanceAndFuel();

            this.saveFuelRecordsData();
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.e("MAIN_ACTIVITY_LOGS", "invalid index to delete: " + position);
        }
    }

    private void saveFuelRecordsData(){
        Log.i("MAIN_ACTIVITY_LOGS", "saveFuelRecordsData");
        try (FileOutputStream fos = context.openFileOutput(MainActivity.dataFilePath, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(this.fuelRecordsModels); // saved whole list
            Log.d("MAIN_ACTIVITY_LOGS", "Saved " + this.fuelRecordsModels.size() + " records");
        } catch (IOException e) {
            Log.i("MAIN_ACTIVITY_LOGS", "saving data failed: " + e);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadFuelRecordsData(){
        Log.i("MAIN_ACTIVITY_LOGS", "loadFuelRecordsData");

        ArrayList<FuelRecordModel> tmpRecordsModels = new ArrayList<>();
        try (FileInputStream fis = context.openFileInput(MainActivity.dataFilePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            tmpRecordsModels = (ArrayList<FuelRecordModel>) ois.readObject(); // read whole list
            Log.d("MAIN_ACTIVITY_LOGS", "Read " + tmpRecordsModels.size() + " records");

        } catch (IOException | ClassNotFoundException e) {
            Log.e("MAIN_ACTIVITY_LOGS", "reading data failed: " + e);
        }

        // reassigned to prevent changing data set for recycle adapter
        this.fuelRecordsModels.addAll(tmpRecordsModels);
        recyclerAdapter.notifyDataSetChanged();
    }
}