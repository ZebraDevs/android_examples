
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Util;

namespace DataWedgeXamarinAndroidApp
{
    [BroadcastReceiver(Name = "com.companyname.DataWedgeXamarinAndroidApp.ScanReceiver")]
	public class ScanReceiver : BroadcastReceiver
	{
 
		private static string DATA_STRING_TAG = "com.symbol.datawedge.data_string";
		
        public static string IntentAction = "com.companyname.DataWedgeXamarinAndroidApp.Data";
        public static string IntentCategory = "android.intent.category.DEFAULT";

		public event EventHandler<String> scanDataReceived;

		public override void OnReceive (Context context, Intent i)
		{
            
			// check the intent action is for us  
			if (i.Action.Equals (IntentAction)) {

				// get the data from the intent  
				String data = i.GetStringExtra (DATA_STRING_TAG);
				

				if (scanDataReceived != null) {
					scanDataReceived (this, data);
				}

			}
		}
	}
}

