package com.example.fuelcalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

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
                startAlreadyInitializedApplication();
            }
        }
    }

    @Override
    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        finishAffinity(); // Closes all activities and ends the application process
        System.exit(0); // exit to ensure that there will be exit if something fails
    }

    private boolean isDataFileExit(){
        // check if data file exit

        return false; // temporary
    }

    private void startInitAppActivity() {
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
                    }
                });

        Intent intent = new Intent(this, InitAppActivity.class);
        initAppActivityResultLauncher.launch(intent);
    }

    private void onInitAppFinished(){
        Log.i("MAIN_ACTIVITY_LOGS", "m_initDataSet: " + m_initDataSet);
        // transform initDataSet to something useful

        this.startAlreadyInitializedApplication();
    }

    private void readDataFile(){

    }

    private void startAlreadyInitializedApplication(){
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
}