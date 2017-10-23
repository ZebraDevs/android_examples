
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
    [Activity(Label = "Activity3", Name = "com.companyname.DataWedgeXamarinAndroidApp.Activity3")]
    public class Activity3 : Activity
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.Activity3Layout);
           
        }
    }
}
