package com.symbol.kitchensinksample;

import android.app.Activity;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKManager.FEATURE_TYPE;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.BarcodeManager.ConnectionState;
import com.symbol.emdk.barcode.BarcodeManager.ScannerConnectionListener;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerInfo;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.StatusData.ScannerStates;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;

/**
 * This class demonstrates the EMDK API to scan the barcode.
 */
public class EMDKScanActivity extends Activity implements
        EMDKListener, DataListener, StatusListener, ScannerConnectionListener {

    String TAG = MainActivity.class.getSimpleName();
    EMDKManager emdkManager = null;
    BarcodeManager barcodeManager = null;
    Scanner scanner = null;

    TextView textViewData = null;
    TextView textViewStatus = null;

    ScrollView emakApiScroll;

    String statusString = "";

    boolean doSoftScanOnce = false;

    //Local variable to indicate that the use requested the configuration change.
    boolean setDecodersRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emdk_scan);

        textViewData = (TextView)findViewById(R.id.textViewData);
        textViewStatus = (TextView)findViewById(R.id.textViewStatus);

        emakApiScroll = (ScrollView) findViewById(R.id.ScrollViewEmdkApi);

        addSoftScanButtonListener();


        //Set this to true to set the apps requested settings for the first time.
        setDecodersRequested = true;

        //Get the EMDK object. This is an asyn call, wait for the EMDKListener.onOpened callback before doing any other action.
        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            displayStatus("EMDKManager object request failed!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // De-initialize scanner if not done already.
        deInitScanner();

        // Release all the resources
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Initialize scanner
        initScanner();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //De-initialize scanner
        deInitScanner();
    }


    @Override
    public void onOpened(EMDKManager emdkManager) {

        Log.d(TAG, "onOpened: " + "EMDK open success!");
        displayStatus("EMDK is ready to use.");

        this.emdkManager = emdkManager;

        initScanner();
    }

    @Override
    public void onClosed() {

        if (emdkManager != null) {

            barcodeManager = null;
            scanner = null;

            // Release all the resources
            emdkManager.release();
            emdkManager = null;
        }
        displayStatus("EMDK closed unexpectedly! Please close and restart the application.");

        Log.d(TAG, "onClosed: " + "EMDK closed unexpectedly! Please close and restart the application.");
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {

        if ((scanDataCollection != null) && (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanData> scanData = scanDataCollection.getScanData();
            for(ScanData data : scanData) {
                String dataString =  data.getData();
                String labelType = data.getLabelType().toString();
                displayData(dataString + " => " + labelType);
            }
        }
    }

    @Override
    public void onStatus(StatusData statusData) {

        ScannerStates state = statusData.getState();
        Log.d(TAG, "onStatus: " + statusData.getFriendlyName()+" is enabled and idle...");
        switch(state) {
            case IDLE:
                statusString = statusData.getFriendlyName()+" is enabled and idle...";
                if(!scanner.isReadPending()) {

                    // Set decoder configuration if the user requested
                    if (setDecodersRequested) {
                        setDecoders();
                        setDecodersRequested = false;
                    }

                    if( doSoftScanOnce ) {
                        scanner.triggerType = Scanner.TriggerType.SOFT_ONCE;
                        doSoftScanOnce = false;
                    }

                    try {
                        scanner.read();
                    } catch (ScannerException e) {
                        e.printStackTrace();
                    }
                }else {
                    statusString = "Previous read is still pending..";
                }
                break;
            case WAITING:
                statusString = "Scanner is waiting for trigger press...";
                break;
            case SCANNING:
                statusString = "Scanning...";
                break;
            case DISABLED:
                statusString = statusData.getFriendlyName()+" is disabled.";

                break;
            case ERROR:
                statusString = "An error has occurred.";
                break;
            default:
                break;
        }

        displayStatus(statusString);
    }

    private void addSoftScanButtonListener() {

        Button btnSoftScan = (Button)findViewById(R.id.buttonSoftScan);

        btnSoftScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (scanner.isEnabled()) {
                    if (scanner.isReadPending()) {
                        doSoftScanOnce = true;
                        try {
                            scanner.cancelRead();
                        } catch (ScannerException e) {
                            e.printStackTrace();
                            displayStatus("" + e.getMessage());
                        }

                    } else {
                        scanner.triggerType = Scanner.TriggerType.SOFT_ONCE;
                        try {
                            scanner.read();
                        } catch (ScannerException e) {
                            e.printStackTrace();
                            displayStatus("" + e.getMessage());
                        }
                    }
                } else {
                    displayStatus("Scanner is not enabled");
                }
            }
        });
    }

    private void setDecoders() {

        try {

            ScannerConfig config = scanner.getConfig();

            //Set ean8
            config.decoderParams.ean8.enabled = false;

            //Set UPCA
            config.decoderParams.upca.enabled = false;

            //set UPCE
            config.decoderParams.upce0.enabled = true;
            config.decoderParams.upce1.enabled = true;

            //Set Code128
            config.decoderParams.code128.enabled = true;

            scanner.setConfig(config);

        } catch (ScannerException e) {
            displayStatus("" + e.getMessage());
            Log.d(TAG, "BarcodeSample1.setDecoders: " + e.getMessage());
        }
    }

    /**
     * Initialize the barcode manager to scan the barcode
     */
    private void initScanner() {

        if(emdkManager != null) {

            // Acquire the barcode manager object
            barcodeManager = (BarcodeManager) emdkManager.getInstance(FEATURE_TYPE.BARCODE);

            // Add connection listener to get the external scanner connection and disconnection notification.
            if (barcodeManager != null) {
                barcodeManager.addConnectionListener(this);
            }

            //Get the scanner object to use the default scanner
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
            if (scanner != null) {

                scanner.addStatusListener(this);
                scanner.addDataListener(this);
                try {
                    scanner.enable();
                } catch (ScannerException e) {
                    displayStatus("" + e.getMessage());
                    Log.d(TAG, "initScanner: " + e.getMessage());
                }
            }else{
                displayStatus("Failed to initialize the scanner device.");
                Log.d(TAG, "initScanner: " + "Failed to initialize the scanner device.");
            }
        }
    }

    /**
     *  Release the barcode manager resources
     */
    private void deInitScanner() {

        if (barcodeManager != null) {
            emdkManager.release(FEATURE_TYPE.BARCODE);
        }
    }

    @Override
    public void onConnectionChange(ScannerInfo scannerInfo, ConnectionState connectionState) {
        switch(connectionState) {
            case CONNECTED:
                initScanner();
                break;
            case DISCONNECTED:
                deInitScanner();
                break;
        }
    }

    //Helper function to display the status string on UI from callbacks
    void displayStatus(String status) {

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            textViewStatus.setText("Status: " + status + "\n");
        } else {
            final String statusTemp = "Status: " + status;
            runOnUiThread(new Runnable() {
                public void run() {
                    textViewStatus.setText(statusTemp);
                }
            });
        }
    }

    //Helper function to display the data string on UI from callbacks
    void displayData(String data) {

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            textViewData.append(data + "\n");
            emakApiScroll.fullScroll(View.FOCUS_DOWN);
        } else {
            final String dataTemp = data;
            runOnUiThread(new Runnable() {
                public void run() {
                    textViewData.append(dataTemp + "\n");
                    emakApiScroll.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }
}
