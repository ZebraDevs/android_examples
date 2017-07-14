package com.symbol.kitchensinksample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * This class demonstrates the use of barcode scanning using the DataWedge service and recieve the data via intent.
  */
public class ScanIntentOutputActivity extends Activity
{
    final String ACTION = "com.symbol.datawedge.api.ACTION";
    final String SWITCH = "com.symbol.datawedge.api.SWITCH_TO_PROFILE";
    final String CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE";
    final String PROFILE_NAME = "PROFILE_NAME";
    final String PROFILE_STATUS = "PROFILE_ENABLED";
    final String CONFIG_MODE = "CONFIG_MODE";
    final String CONFIG_MODE_UPDATE = "UPDATE";
    final String CONFIG_MODE_CREATE = "CREATE_IF_NOT_EXIST";
    final String SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";

    final String  SOFT_SCAN_TRIGGER = "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER";
    final String START_SCANNING = "START_SCANNING";

    TextView dataTextView;
    ScrollView barcodeScroll;

    private final String DW_PKG_NAME = "com.symbol.datawedge";
    private final String DW_INTENT_SUPPORT_VERSION = "6.3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_intent_output);

        dataTextView = (TextView) findViewById(R.id.textView14);

        dataTextView.setSelected(true);
        dataTextView.setMovementMethod(new ScrollingMovementMethod());

        barcodeScroll = (ScrollView) findViewById(R.id.ScrollViewScanIntent);

        addSoftScanButtonListener();

        //All the DataWedge version does not support creating the profile using the DataWedge intent API.
        //To avoid crashes on the device, make sure to check the DtaaWedge version before creating the profile.
        int result = -1;
        String versionCurrent="";
        // Find out current DW version, if the version is 6.3 or higher then we know it support intent config
        // Then we can send CartScan profile via intent
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(DW_PKG_NAME, PackageManager.GET_META_DATA);
            versionCurrent = pInfo.versionName;
            Log.i(TAG, "createProfileInDW: versionCurrent=" + versionCurrent);

            result = compareVersionString(versionCurrent, DW_INTENT_SUPPORT_VERSION);
            Log.i(TAG, "onCreate: result=" + result);
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e(TAG, "onCreate: NameNotFoundException:", e1);
        }
        
        if (result >= 0) {
            createDataWedgeProfile();
        } else {
            dataTextView.append("DataWedge version is " + versionCurrent + ", " +
                    "but the current Sample is only supported with DataWedge version 6.3 or greater.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Register for the intent to receiev the scanned data using intent callabck.
        //The action and category name used must be same as the names usied in the profile creation.
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.symbol.kitchensinksample.ScanIntentOutputActivity");
        filter.addCategory("android.intent.category.DEFAULT");
        registerReceiver(mybroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Unregister the intent reciever when the app goes to background.
        unregisterReceiver(mybroadcastReceiver);
    }

    /**
     * This code helps to scan the barcode by clicking the soft button used in the app instead of hardware scanner trigger.
     */
    private void addSoftScanButtonListener() {
        Button softScanButton = (Button) findViewById(R.id.buttonDWSoftScan);
        softScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent();
                i.setAction(ACTION);
                i.putExtra(SOFT_SCAN_TRIGGER, START_SCANNING);
                sendBroadcast(i);
            }
        });
    }

    /**
     * This code demonstrates how to create the DataWedge programatically and modify the settings.
     * This code can be skipped if the profile is created on the DataWedge manaually and pushed to different device though MDM
     */
    public void createDataWedgeProfile()
    {
        //Create profile if doesn't exit and update the required settings
        {
            Bundle configBundle = new Bundle();
            Bundle bConfig = new Bundle();
            Bundle bParams = new Bundle();
            Bundle bundleApp1 = new Bundle();

            bParams.putString("scanner_selection", "auto");
            bParams.putString("intent_output_enabled", "true");
            bParams.putString("intent_action", "com.symbol.kitchensinksample.ScanIntentOutputActivity");
            bParams.putString("intent_category", "android.intent.category.DEFAULT");
            bParams.putString("intent_delivery", "2");

            configBundle.putString(PROFILE_NAME, "KitchenSinkApp");
            configBundle.putString(PROFILE_STATUS, "true");
            configBundle.putString(CONFIG_MODE, CONFIG_MODE_CREATE);

            bundleApp1.putString("PACKAGE_NAME", "com.symbol.kitchensinksample");
            bundleApp1.putStringArray("ACTIVITY_LIST", new String[]{"com.symbol.kitchensinksample.ScanIntentOutputActivity"});


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

        //TO recieve the scanned via intent, the keystroke must disabled.
        {
            Bundle configBundle = new Bundle();
            Bundle bConfig = new Bundle();
            Bundle bParams = new Bundle();

            bParams.putString("keystroke_output_enabled", "false");

            configBundle.putString(PROFILE_NAME, "KitchenSinkApp");
            configBundle.putString(PROFILE_STATUS, "true");
            configBundle.putString(CONFIG_MODE, CONFIG_MODE_UPDATE);

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

    //Broadcast Receiver for recieving the intents back from DataWedge
    private BroadcastReceiver mybroadcastReceiver   = new BroadcastReceiver () {
        final String LABEL_TYPE_TAG = "com.symbol.datawedge.label_type";
        final String DATA_STRING_TAG = "com.symbol.datawedge.data_string";
      //  final String DECODE_DATA_TAG = "com.symbol.datawedge.decode_data";

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                String labelType = intent.getStringExtra(LABEL_TYPE_TAG);
                String decodeString1 = intent.getStringExtra(DATA_STRING_TAG);
               // String decodeData = intent.getStringExtra(DATA_STRING_TAG);
                //dataTextView.append(decodeString1.toString() + "\t" + labelType + "\n");
                dataTextView.append(labelType + "\t" + decodeString1.toString() + "\n");

                barcodeScroll.fullScroll(View.FOCUS_DOWN);
            }
        }
    };

    //DataWedge version comparision
    private int compareVersionString(String v1, String v2) {

        try {

            if (v1.equals(v2)) {
                return 0;
            }

            if (v1.length() == 0 || v2.length() == 0) {
                return -1;
            }

            v1 = v1.replaceAll("\\s", "");
            v2 = v2.replaceAll("\\s", "");
            String[] a1 = v1.split("\\.");
            String[] a2 = v2.split("\\.");
            List<String> l1 = Arrays.asList(a1);
            List<String> l2 = Arrays.asList(a2);

            int i = 0;
            while (true) {
                Double d1 = null;
                Double d2 = null;

                try{
                    String temp1 = l1.get(i).replaceAll("[\\D]", "");

                    String split1[] = l1.get(i).split("[\\D]");
                    if(split1 != null) {
                        temp1 = split1[0];
                    }

                    d1 = Double.parseDouble(temp1);
                }catch(IndexOutOfBoundsException e){
                    if(e !=null) {
                        Log.d(TAG, "Exception: " + e.getMessage());
                    }
                } catch(NumberFormatException e) {
                    if(e !=null) {
                        Log.d(TAG, "Exception: " + e.getMessage());
                    }
                }

                try{
                    String temp2 = l2.get(i).replaceAll("[\\D]", "");

                    String split2[] = l2.get(i).split("[\\D]");
                    if(split2 != null) {
                        temp2 = split2[0];
                    }
                    d2 = Double.parseDouble(temp2);
                }catch(IndexOutOfBoundsException e){
                    if(e !=null) {
                        Log.d(TAG, "Exception: " + e.getMessage());
                    }
                }catch(NumberFormatException e) {
                    if(e !=null) {
                        Log.d(TAG, "Exception: " + e.getMessage());
                    }
                }

                Log.d("VersionCheck", "d1==== " + d1);
                Log.d("VersionCheck", "d2==== " + d2);
                if (d1 != null && d2 != null) {
                    if (d1.doubleValue() > d2.doubleValue()) {
                        return 1;
                    } else if (d1.doubleValue() < d2.doubleValue()) {
                        return -1;
                    }
                } else if (d2 == null && d1 != null) {
                    if (d1.doubleValue() > 0) {
                        return 1;
                    }
                } else if (d1 == null && d2 != null) {
                    if (d2.doubleValue() > 0) {
                        return -1;
                    }
                } else {
                    break;
                }
                i++;
            }

        } catch(Exception ex) {
            if(ex !=null) {
                Log.d(TAG, "Exception: " + ex.getMessage());
            }
        }
        return 0;
    }
}
