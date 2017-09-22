package com.symbol.datawedgedroidconnyc2017;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {


    final String ProfileName = "DroidconNyc2017";
    final String barcodeDataAction = "com.symbol.DataWedgeDroidconNyc2017";

    BarcodeDataReceiver barcodeDataReceiver = new BarcodeDataReceiver();


    EditText scanDataEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scanDataEntry = (EditText) findViewById(R.id.scan_data_entry);

        Button softScanButton = (Button) findViewById(R.id.scan_data_button);

        softScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                softScan();
            }
        });

        Switch profileEnableSwitch = (Switch) findViewById(R.id.profile_enable_switch);

        profileEnableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                configureProfile(isChecked);

            }
        });


        createDataWedgeProfile();
    }


    @Override
    protected void onResume() {
        super.onResume();

        registerReceivers();

    }

    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(barcodeDataReceiver);
    }

    public void softScan() {
        String ACTION = "com.symbol.datawedge.api.ACTION";
        String SOFT_SCAN_TRIGGER = "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER";
        String START_SCANNING = "START_SCANNING";

        Intent i = new Intent();
        i.setAction(ACTION);
        i.putExtra(SOFT_SCAN_TRIGGER, START_SCANNING);
        sendBroadcast(i);
    }


    private void registerReceivers() {
        IntentFilter barcodeDataFilter = new IntentFilter(barcodeDataAction);
        barcodeDataFilter.addCategory("android.intent.category.DEFAULT");
        this.registerReceiver(barcodeDataReceiver, barcodeDataFilter);
    }

    public class BarcodeDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent i) {

            final String DATA_STRING_TAG = "com.symbol.datawedge.data_string";
            final String LABEL_TYPE_TAG = "com.symbol.datawedge.label_type";

            if (i.getAction().equals(barcodeDataAction)) {

                String labelType = i.getStringExtra(LABEL_TYPE_TAG);
                String barcode_data = i.getStringExtra(DATA_STRING_TAG);

                scanDataEntry.append(labelType + "\t\t" + barcode_data.toString() + "\n");

            }
        }
    }


    public void createDataWedgeProfile() {
        String ACTION = "com.symbol.datawedge.api.ACTION";
        String SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";

        {
            Bundle configBundle = new Bundle();
            Bundle bConfig = new Bundle();
            Bundle bParams = new Bundle();
            Bundle bundleApp1 = new Bundle();

            bParams.putString("scanner_selection", "auto");
            bParams.putString("intent_output_enabled", "true");
            bParams.putString("intent_action", barcodeDataAction);
            bParams.putString("intent_category", "android.intent.category.DEFAULT");
            bParams.putString("intent_delivery", "2");

            configBundle.putString("PROFILE_NAME", ProfileName);
            configBundle.putString("PROFILE_ENABLED", "true");
            configBundle.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");

            bundleApp1.putString("PACKAGE_NAME", "com.symbol.datawedgedroidconnyc2017");
            bundleApp1.putStringArray("ACTIVITY_LIST", new String[]{"*"});


            configBundle.putParcelableArray("APP_LIST", new Bundle[]{bundleApp1});

            bConfig.putString("PLUGIN_NAME", "INTENT");
            bConfig.putString("RESET_CONFIG", "false");

            bConfig.putBundle("PARAM_LIST", bParams);
            configBundle.putBundle("PLUGIN_CONFIG", bConfig);

            Intent i = new Intent();
            i.setAction(ACTION);
            i.putExtra(SET_CONFIG, configBundle);
            this.sendBroadcast(i);
        }

        {
            Bundle configBundle = new Bundle();
            Bundle bConfig = new Bundle();
            Bundle bParams = new Bundle();

            bParams.putString("keystroke_output_enabled", "false");

            configBundle.putString("PROFILE_NAME", ProfileName);
            configBundle.putString("PROFILE_ENABLED", "true");
            configBundle.putString("CONFIG_MODE", "UPDATE");

            bConfig.putString("PLUGIN_NAME", "KEYSTROKE");
            bConfig.putString("RESET_CONFIG", "false");

            bConfig.putBundle("PARAM_LIST", bParams);
            configBundle.putBundle("PLUGIN_CONFIG", bConfig);

            Intent i = new Intent();
            i.setAction(ACTION);
            i.putExtra(SET_CONFIG, configBundle);
            this.sendBroadcast(i);
        }

    }

    public void configureProfile(boolean enabled) {
        String ACTION = "com.symbol.datawedge.api.ACTION";
        String SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";
        Bundle configBundle = new Bundle();

        configBundle.putString("PROFILE_NAME", ProfileName);
        configBundle.putString("PROFILE_ENABLED", String.valueOf(enabled));
        configBundle.putString("CONFIG_MODE", "UPDATE");


        Intent i = new Intent();
        i.setAction(ACTION);
        i.putExtra(SET_CONFIG, configBundle);
        this.sendBroadcast(i);

    }


}
