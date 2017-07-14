package com.symbol.kitchensinksample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;

/**
 * This class demonstrates how to use the EMDK profile Manager to modify device settings on demand.
 */
public class TouchManagerActivity extends Activity implements EMDKManager.EMDKListener, ProfileManager.DataListener {
    private static final String TAG = TouchManagerActivity.class.getSimpleName();

    //Assign the profile name used in EMDKConfig.xml
    private String stylusAndGloveProfileName = "StylusAndGlove";
    private String gloveAndFingerProfileName = "GloveAndFinger";

    //Declare a variable to store ProfileManager object
    private  ProfileManager profileManager;

    //Declare a variable to store EMDKManager object
    private EMDKManager emdkManager;

    RadioGroup radionButtonTouchPanelMode;

    // Provides the error type for characteristic-error
    private String errorType = "";

    // Provides the parm name for parm-error
    private String parmName = "";

    // Provides error description
    private String errorDescription = "";

    // Provides error string with type/name + description
    private String errorString = "";

    private TextView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_manager);

        statusView = (TextView)findViewById(R.id.textViewProfileStatus);

        addRadioButtonListener();

        //The EMDKManager object will be created and returned in the callback.
        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);

        //Check the return status of EMDKManager object creation.
        if(results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
            //EMDKManager object creation success
            statusView.setText("EMDK initialization in progress...");
        }else {
            //EMDKManager object creation failed
            statusView.setText("EMDK initialization in failed");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Clean up the objects created by EMDK manager
        if (emdkManager != null) {
            emdkManager.release();
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;
        //Get the ProfileManager object to process the profiles
        profileManager = (ProfileManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
        profileManager.addDataListener(this);

        displayMessage("EMDK initialized and app is ready to use.");
    }

    @Override
    public void onClosed() {
        displayMessage("EMDK closed unexpectedly. Restart the app.");
    }

    void addRadioButtonListener() {
        radionButtonTouchPanelMode = (RadioGroup) findViewById(R.id.radioGroupTouchManager);
        radionButtonTouchPanelMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(profileManager != null) {
                    View radioButton = radionButtonTouchPanelMode.findViewById(checkedId);
                    int index = radionButtonTouchPanelMode.indexOfChild(radioButton);
                    EMDKResults results = null;
                    switch (index) {
                        case 0: // Stylus and Glove selected
                            Log.d(TAG,  "Stylus and Glove selected: " + index);
                            results = profileManager.processProfileAsync(stylusAndGloveProfileName, ProfileManager.PROFILE_FLAG.SET, (String[])null);
                            break;
                        case 1: // Finger and Glove selected
                            results = profileManager.processProfileAsync(gloveAndFingerProfileName, ProfileManager.PROFILE_FLAG.SET, (String[])null);
                            Log.d(TAG,  "Finger and Glove selected: " + index);
                            break;
                    }
                    statusView.setText(results.statusCode.toString());
                }
            }
        });
    }


    @Override
    public void onData(ProfileManager.ResultData resultData) {

     String resultString = "";
        EMDKResults emdkResults = resultData.getResult();

        //Check the return status of processProfile
        if(emdkResults.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {

            // Get XML response as a String
            String statusXMLResponse = emdkResults.getStatusString();

            try {
                // Create instance of XML Pull Parser to parse the response
                XmlPullParser parser = Xml.newPullParser();
                // Provide the string response to the String Reader that reads
                // for the parser
                parser.setInput(new StringReader(statusXMLResponse));
                // Call method to parse the response
                parseXML(parser);

                if ( TextUtils.isEmpty(parmName) && TextUtils.isEmpty(errorType) && TextUtils.isEmpty(errorDescription) ) {

                    resultString = "Profile update success.";
                }
                else {

                    resultString = "Profile update failed." + errorString;
                }

            } catch (XmlPullParserException e) {
                resultString =  e.getMessage();
            }
        }
        displayMessage(resultString);
    }

    //Helper function to display the status string on UI from callbacks
    void displayMessage(String status) {

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            statusView.setText(status + "\n");
        } else {
            final String statusTemp = status;
            runOnUiThread(new Runnable() {
                public void run() {
                    statusView.setText(statusTemp);
                }
            });
        }
    }

    // Method to parse the XML response using XML Pull Parser
    public void parseXML(XmlPullParser myParser) {
        int event;
        try {
            // Retrieve error details if parm-error/characteristic-error in the response XML
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:

                        if (name.equals("parm-error")) {
                            parmName = myParser.getAttributeValue(null, "name");
                            errorDescription = myParser.getAttributeValue(null, "desc");
                            errorString = " (Name: " + parmName + ", Error Description: " + errorDescription + ")";
                            return;
                        }

                        if (name.equals("characteristic-error")) {
                            errorType = myParser.getAttributeValue(null, "type");
                            errorDescription = myParser.getAttributeValue(null, "desc");
                            errorString = " (Type: " + errorType + ", Error Description: " + errorDescription + ")";
                            return;
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        break;
                }
                event = myParser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
