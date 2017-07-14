package com.symbol.kitchensinksample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This class helps to display the list features supported by the sample when the activity is launched
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView1;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO:  Later this list could be read from a XML file
        FeatureInfo feature_list[] = new FeatureInfo[]
                {
                    // Set sample icon and sample's title
                        new FeatureInfo(R.drawable.device_info, "Device Info"),
                        new FeatureInfo(R.drawable.battery, "Battery Info"),
                        new FeatureInfo(R.drawable.datawedge, "Scan: Keystroke Output"),
                        new FeatureInfo(R.drawable.datawedge, "Scan: Intent Output"),
                        new FeatureInfo(R.drawable.emdk, "Scan: EMDK API"),
                        new FeatureInfo(R.drawable.touch, "Touch Manager"),
                        new FeatureInfo(R.drawable.sensor, "Sensor Manager")
                };

        FeatureViewAdapter adapter = new FeatureViewAdapter(this,
                R.layout.listview_item_row, feature_list);
        listView1 = (ListView) findViewById(R.id.listView1);

        listView1.setAdapter(adapter);


        // Handle Click event for single list row
        listView1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {

                    case 0:
                        Log.d(TAG, "Device Info Requested");
                        Intent newActivity = new Intent(MainActivity.this, DeviceInfoActivity.class);
                        startActivity(newActivity);
                        break;

                    case 1:
                        Log.d(TAG, "Battery Info Requested");
                        Intent batteryActivity = new Intent(MainActivity.this, BatteryInfoActivity.class);
                        startActivity(batteryActivity);
                        break;

                    case 2:
                        Log.d(TAG, "Scan using DataWedge Keystroke Output");
                        Intent dwScanKeystrokeActivity = new Intent(MainActivity.this, ScanKeystrokeOutputActivity.class);
                        startActivity(dwScanKeystrokeActivity);
                        break;

                    case 3:
                        Log.d(TAG, "Scan using DataWedge Intent Output Requested");
                        Intent dwScanIntentActivity = new Intent(MainActivity.this, ScanIntentOutputActivity.class);
                        startActivity(dwScanIntentActivity);
                        break;

                    case 4:
                        Log.d(TAG, "Scanning using EMDK API requested");
                        Intent emdkActivity = new Intent(MainActivity.this, EMDKScanActivity.class);
                        startActivity(emdkActivity);
                        break;

                    case 5:
                        Log.d(TAG, "Touch Manager requested");
                        Intent touchManagerActivity = new Intent(MainActivity.this, TouchManagerActivity.class);
                        startActivity(touchManagerActivity);
                        break;

                    case 6:
                        Intent sensorActivity = new Intent(MainActivity.this, SensorActivity.class);
                        startActivity(sensorActivity);
                        Log.d(TAG, "Sensor Reading");
                        break;
                }
            }

        });
    }

}
