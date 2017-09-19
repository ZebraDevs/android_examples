using System;
using Xamarin.Forms;
using Android.Util;
using Android.Content;
using Android.App;
using Android.OS;

[assembly: Dependency(typeof(DataWedgeFormsApp.Scanning))]
namespace DataWedgeFormsApp
{
    public class Scanning: IScanning
	{
        public static string TAG = "DataWedgeFormsApp - IScanning Interface";
        
        // define DataWedge API strings
        private static String PROFILE_NAME = "PROFILE_NAME";
        private static String PROFILE_STATUS = "PROFILE_ENABLED";
        private static String CONFIG_MODE = "CONFIG_MODE";
        private static String CONFIG_MODE_UPDATE = "UPDATE";
        private static String ACTION = "com.symbol.datawedge.api.ACTION";
        private static String SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";
        private static String SWITCH = "com.symbol.datawedge.api.SWITCH_TO_PROFILE";
        private static String CONFIG_MODE_CREATE = "CREATE_IF_NOT_EXIST";

        Activity currentActivity = (Activity)Forms.Context;

        // this event will be triggered when data is recieved in the DataWedge broadcast receiver
        public event EventHandler<String> OnData;


        public void EnableIntentScanning(){

            // turn off keystroke scanning when intent scanning is enabled
            DisableKeystrokeScanning();

            // define a few bundles to contain datawedge profile configuration values
            Bundle configBundle = new Bundle();
			Bundle bConfig = new Bundle();
			Bundle bParams = new Bundle();

            // turn on intent output
            bParams.PutString("intent_output_enabled", "true");

            // select the profile we want to update
            configBundle.PutString(PROFILE_NAME, "DWFormsApp"); 

            // enable the profile
            configBundle.PutString(PROFILE_STATUS, "true");

            // set the configuration mode to update
            configBundle.PutString(CONFIG_MODE, CONFIG_MODE_UPDATE);

			//identify the datawedge plugin we want to configure 
            bConfig.PutString("PLUGIN_NAME", "INTENT");
			bConfig.PutString("RESET_CONFIG", "false");

            // attach the parameter list for the plugin
			bConfig.PutBundle("PARAM_LIST", bParams);
			configBundle.PutBundle("PLUGIN_CONFIG", bConfig);

            // create a new intent
            Intent i = new Intent();

            // set the action to the DataWedge Intent API action defined above
            i.SetAction(ACTION);

            // attach the config bundle 
            i.PutExtra(SET_CONFIG, configBundle);

            //broadcast the intent
            currentActivity.SendBroadcast(i);

            {
                // insure our configured datawedge profile is selected
                SwitchToProfile("DWFormsApp");
            }
            
        }

        public void DisableIntentScanning(){
            
			// define a few bundles to contain datawedge profile configuration values
			Bundle configBundle = new Bundle();
			Bundle bConfig = new Bundle();
			Bundle bParams = new Bundle();
			
            // turn on intent output
			bParams.PutString("intent_output_enabled", "false");

			// select the profile we want to update
			configBundle.PutString(PROFILE_NAME, "DWFormsApp"); 

            // disable the profile
            configBundle.PutString(PROFILE_STATUS, "false");

			// set the configuration mode to update
			configBundle.PutString(CONFIG_MODE, CONFIG_MODE_UPDATE);

			//identify the datawedge plugin we want to configure 
			bConfig.PutString("PLUGIN_NAME", "INTENT");
			bConfig.PutString("RESET_CONFIG", "false");

			// attach the parameter list for the plugin    
			bConfig.PutBundle("PARAM_LIST", bParams);
			configBundle.PutBundle("PLUGIN_CONFIG", bConfig);

			// create a new intent
			Intent i = new Intent();

			// set the action to the DataWedge Intent API action defined above
			i.SetAction(ACTION);

			// attach the config bundle 
			i.PutExtra(SET_CONFIG, configBundle);

			//broadcast the intent
			currentActivity.SendBroadcast(i); 
        }


        public void EnableKeystrokeScanning(){

            // disable intent scanning while keystroke scanning is enabled
            DisableIntentScanning();

			// define a few bundles to contain datawedge profile configuration values
			Bundle configBundle = new Bundle();
            Bundle bConfig = new Bundle();
            Bundle bParams = new Bundle();

            // enable keystroke scanning
            bParams.PutString("keystroke_output_enabled", "true");

			// select the profile we want to update
			configBundle.PutString(PROFILE_NAME, "DWFormsApp"); 

            //enable the profile
            configBundle.PutString(PROFILE_STATUS, "true");

			// set the configuration mode to update
			configBundle.PutString(CONFIG_MODE, CONFIG_MODE_UPDATE);

			//identify the datawedge plugin we want to configure 
			bConfig.PutString("PLUGIN_NAME", "KEYSTROKE");
            bConfig.PutString("RESET_CONFIG", "false");

			// attach the parameter list for the plugin
			bConfig.PutBundle("PARAM_LIST", bParams);
            configBundle.PutBundle("PLUGIN_CONFIG", bConfig);

			// create a new intent
			Intent i = new Intent();

			// set the action to the DataWedge Intent API action defined above
			i.SetAction(ACTION);

			// attach the config bundle 
			i.PutExtra(SET_CONFIG, configBundle);

			//broadcast the intent
			currentActivity.SendBroadcast(i);

			{
				// insure our configured datawedge profile is selected
				SwitchToProfile("DWFormsApp");
			}
            
        }

        public void DisableKeystrokeScanning(){
            
			// define a few bundles to contain datawedge profile configuration values
			Bundle configBundle = new Bundle();
            Bundle bConfig = new Bundle();
            Bundle bParams = new Bundle();

            //turn off keystroke output
            bParams.PutString("keystroke_output_enabled", "false");

			// select the profile we want to update
			configBundle.PutString(PROFILE_NAME, "DWFormsApp"); 

            // disable the profile
            configBundle.PutString(PROFILE_STATUS, "false");

			// set the configuration mode to update
			configBundle.PutString(CONFIG_MODE, CONFIG_MODE_UPDATE);

			//identify the datawedge plugin we want to configure 
			bConfig.PutString("PLUGIN_NAME", "KEYSTROKE");
            bConfig.PutString("RESET_CONFIG", "false");

			// attach the parameter list for the plugin
			bConfig.PutBundle("PARAM_LIST", bParams);
            configBundle.PutBundle("PLUGIN_CONFIG", bConfig);

			// create a new intent
			Intent i = new Intent();

			// set the action to the DataWedge Intent API action defined above
			i.SetAction(ACTION);

			// attach the config bundle 
			i.PutExtra(SET_CONFIG, configBundle);

			//broadcast the intent
			currentActivity.SendBroadcast(i); 
            
        }

        // this method can be used to swictch between existing datawedge profiles
        private void SwitchToProfile(String profile_name){
            
			// create a new intent
			Intent i = new Intent();

			// set the action to the DataWedge Intent API action defined above
			i.SetAction(ACTION);

            //Set action we want to perform to switch and provide profile name
            i.PutExtra(SWITCH, profile_name);

            // send broadcast
            currentActivity.SendBroadcast(i);
        }


        // this method is called on application start create our datawedge profile if it does not exist
        public void CreateDataWedgeProfile(){

			    // define a few bundles to contain datawedge profile configuration values
				Bundle configBundle = new Bundle();
				Bundle bConfig = new Bundle();
				Bundle bParams = new Bundle();
				
                // allow default scanner to be selected
				bParams.PutString("scanner_selection", "auto");

			    //set the intnet action that will be filtered for in our broadcast reciver when intent output is enabled	
                bParams.PutString("intent_action", "com.symbol.DataWedgeFormsApp");
			    
                //set the intnet catagory that will be filtered for in our broadcast reciver when intent output is enabled
			    bParams.PutString("intent_category", "android.intent.category.DEFAULT");


    			//Intent output types: 0 = startActivity, 1 = Send via StartService, 2 = Broadcast intnet
    			//set intent output to broadcast
    			bParams.PutString("intent_delivery", "2");

                //define the name of our profile
				configBundle.PutString(PROFILE_NAME, "DWFormsApp");
                //set our mode to create
				configBundle.PutString(CONFIG_MODE, CONFIG_MODE_CREATE);

			    //identify the datawedge plugin we want to configure 
			    bConfig.PutString("PLUGIN_NAME", "INTENT");
				bConfig.PutString("RESET_CONFIG", "false");
			
                // attach the parameter list for the plugin
			    bConfig.PutBundle("PARAM_LIST", bParams);
    			configBundle.PutBundle("PLUGIN_CONFIG", bConfig);

			    // create a new intent
			    Intent i = new Intent();

			    // set the action to the DataWedge Intent API action defined above
			    i.SetAction(ACTION);

                //attach the config bundle 
				i.PutExtra(SET_CONFIG, configBundle);

                //broadcast the intent
				currentActivity.SendBroadcast(i);
			
        }

        // this method is called by the DataWedge broadcast receiver to invoke the Ondata event
        public void TriggerOnDataEvent(String data){
            Log.Debug(TAG, data);
            OnData?.Invoke(this, data);
        }
	}
}
