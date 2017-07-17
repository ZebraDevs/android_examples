package com.symbol.kitchensinksample;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import static com.symbol.kitchensinksample.R.id.listViewDeviceInfo;

/**
 * This class demonstrates how to get the device information.
 */
public class DeviceInfoActivity extends Activity {
    private static final String TAG = DeviceInfoActivity.class.getSimpleName();
    private ListView lvDeviceInfo;

    String version = "";
    String dwPakgName = "com.symbol.datawedge";
    String mxVersionPkgName = "com.symbol.mxmf.csp.mx";
    String emdkPkgName = "com.symbol.emdk.emdkservice";
    PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        packageManager = getPackageManager();
        InfoList info_list[] = new InfoList[]
                {
                        // Set sample icon and sample's title
                        new InfoList("Manufacturer: ", getDeviceManufacturer()),
                        //new InfoList("Device: ", getDevice()),
                        new InfoList("Model: ", getDeviceModelNumber()),
                        new InfoList("Serial No.: ", getDeviceSerialNumber()),
                        new InfoList("OS: ", getOS()),
                        new InfoList("Android SDK: ",String.valueOf(getSDK())),
                        new InfoList("DataWedge: ", getDW()),
                        new InfoList("EMDK: ", getEMDK()),
                        new InfoList("MX: ", getMX())

                };
        KeyValueViewAdapter adapter = new KeyValueViewAdapter(this,
                R.layout.listview_item_key_value, info_list);

        lvDeviceInfo = (ListView) findViewById(listViewDeviceInfo);

        lvDeviceInfo.setAdapter(adapter);
    }

    /**
     * Get the manufacturer of this device
     * @return a string identifying the manufacturer
     */
    private String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * Get the device
     * @return a string identifying the device
     */
    private String getDevice() {
        return Build.DEVICE;
    }

    /**
     * Get the model number of this device
     * @return a string identifying the model number of the device.
     */
    private String getDeviceModelNumber() {
        return Build.MODEL;
    }

    /**
     * Get the serial number of this device
     * @return a string identifying the serial number of this device
     */
    private String getDeviceSerialNumber() {
        return Build.SERIAL;
    }


    /**
     * Get the Android OS version
     * @return a string identifying the Android OS version
     */
    private String getOS() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Get the latest SDK version supported by this device
     * @return an integer representing the SDK level
     */
    private int getSDK() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * Get the DataWedge version
     * @return DataWedge version on the device
     */
    private String getDW() {
        //DataWedge Version
        version = "";
            try {
            PackageInfo info = packageManager.getPackageInfo(dwPakgName, 0);
            if (info != null) {
                version = info.versionName;
            }
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
        }

        return version;
    }

    /**
     * Get the EMDK version
     * @return EMDK version on the device
     */
    private String getEMDK() {
        version = "";
        try {
            PackageInfo info = packageManager.getPackageInfo(emdkPkgName, 0);
            if (info != null) {
                version = info.versionName;
            }
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
        }
        return version;
    }


    /**
     * Get the MX version
     * @return MX version on the device
     */
    private String getMX() {
        //MX version
        version = "";
        try {
            PackageInfo info = packageManager.getPackageInfo(mxVersionPkgName,
                    0);
            if (info != null) {
                version = info.versionName;
            }
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
        }
        return version;
    }
}
