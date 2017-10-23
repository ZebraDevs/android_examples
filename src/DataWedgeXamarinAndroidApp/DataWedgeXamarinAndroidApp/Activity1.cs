
using System;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Util;
using Android.Widget;



namespace DataWedgeXamarinAndroidApp
{
    [Activity(Label = "DataWedgeXamarinAndroidApp", MainLauncher = true, Name = "com.companyname.DataWedgeXamarinAndroidApp.Activity1")]
    public class Activity1 : Activity
    {
       
        ScanReceiver dwRcvr;

        EditText EditText1;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.Activity1Layout);

            EditText1 = FindViewById<EditText>(Resource.Id.editText1);

            dwRcvr = new ScanReceiver();

            dwRcvr.scanDataReceived += (s, scanData) =>
            {
                EditText1.Text = scanData;

            };


            Button button = FindViewById<Button>(Resource.Id.button1);

            button.Click += delegate { this.StartActivity(new Intent(this,typeof(Activity2))); };

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
