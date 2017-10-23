
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
using Xamarin.Android;
using Android.Util;


namespace DataWedgeFormsApp.Droid
{
   [BroadcastReceiver(Name = "com.companyname.DataWedgeFormsApp.DataWedgeReceiver")]
    public class DataWedgeReceiver : BroadcastReceiver
    {
        public static string TAG = "DataWedgeFormsApp - DataWedgeReceiver";
 
	    // Define DataWedge Intent API strings
		private static string DATA_STRING_TAG = "com.motorolasolutions.emdk.datawedge.data_string";
		public static string IntentAction = "com.symbol.DataWedgeFormsApp";
		public static string IntentCategory = "android.intent.category.DEFAULT";


        public override void OnReceive(Context context, Intent i)
        {

			if (i.Action.Equals(IntentAction))
			{
                // get the data from the intent  
				String data = i.GetStringExtra(DATA_STRING_TAG);

                // check that we have barcode data
                if (data != null && data.Length > 0)
                {
                    // log the barcode data if needed
                    Log.Debug(TAG, data);

                    // get an instance of the IScanning dependencyService
                    var dpSrvc = DependencyService.Get<IScanning>();

                    // call method that triggers an the OnData event ( event attached to xamarin form )
                    dpSrvc.TriggerOnDataEvent(data);
                }

			}
        }
    }
}
