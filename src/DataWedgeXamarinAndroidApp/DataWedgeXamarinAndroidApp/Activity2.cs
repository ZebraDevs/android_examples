
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

namespace DataWedgeXamarinAndroidApp
{
    [Activity(Label = "Activity2", Name = "com.companyname.DataWedgeXamarinAndroidApp.Activity2")]
    public class Activity2 : Activity
    {

        ScanReceiver dwRcvr;

        EditText EditText2;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.Activity2Layout);

            EditText2 = FindViewById<EditText>(Resource.Id.editText2);

            dwRcvr = new ScanReceiver();

            dwRcvr.scanDataReceived += (s, scanData) =>
            {
                EditText2.Text = scanData;

            };

            Button button = FindViewById<Button>(Resource.Id.button1);

            button.Click += delegate { this.StartActivity(new Intent(this, typeof(Activity3))); };
        }

        protected override void OnResume()
        {
            base.OnResume();
            IntentFilter dwFilter = new IntentFilter(ScanReceiver.IntentAction);
            dwFilter.AddCategory(ScanReceiver.IntentCategory);
            RegisterReceiver(dwRcvr, dwFilter);

        }


        protected override void OnPause()
        {
            UnregisterReceiver(dwRcvr);
            base.OnPause();
        }
    }
}
