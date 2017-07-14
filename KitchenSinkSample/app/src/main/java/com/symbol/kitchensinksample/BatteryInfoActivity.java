package com.symbol.kitchensinksample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ListView;

import static com.symbol.kitchensinksample.R.id.listViewBatteryInfo;

/**
 * This activity demonstrates how to obtain the battery information of the device.
 */
public class BatteryInfoActivity extends Activity {
    private ListView lvBatteryInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_info);

        lvBatteryInfo = (ListView) findViewById(listViewBatteryInfo);

        //Register for battery status
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryIntent = registerReceiver(broadcastReceiver, intentFilter);

        displayBatteryInfo(batteryIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            displayBatteryInfo(intent);
        }
    };

    void displayBatteryInfo(Intent intent) {

        //Get the Battery Information from the Intent
        float level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        String serialnumber = intent.getExtras().getString("serialnumber");
        String mfd = intent.getExtras().getString("mfd");
        String partnumber = intent.getExtras().getString("partnumber");
        int bkvoltage = intent.getExtras().getInt("bkvoltage");
        int ratedcapacity = intent.getExtras().getInt("ratedcapacity");
        int cycle = intent.getExtras().getInt("cycle");

        //Create an InfoList to display the data on the UI
        InfoList info_list[] = new InfoList[]
            {
                    // Display key and value
                    new InfoList(getString(R.string.battery_serial_number), serialnumber),
                    new InfoList(getString(R.string.battery_mfd), mfd),
                    new InfoList(getString(R.string.battery_part_number), partnumber),
                    new InfoList(getString(R.string.battery_level), String.valueOf(level)),
                    new InfoList(getString(R.string.battery_voltage), String.valueOf(bkvoltage)),
                    new InfoList(getString(R.string.battery_rated_capacity), String.valueOf(ratedcapacity)),
                    new InfoList(getString(R.string.battery_cycle), String.valueOf(cycle)),
            };

        KeyValueViewAdapter adapter = new KeyValueViewAdapter(this, R.layout.listview_item_key_value, info_list);

        //Update the listview adpater.
        lvBatteryInfo.setAdapter(adapter);
    }
}
