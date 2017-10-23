using Android.App;
using Android.Widget;
using Android.OS;
using Android.Content;

namespace DataWedgeXamarinAndroidApp
{
    [Activity(Label = "DataWedgeXamarinAndroidApp", MainLauncher = true, Icon = "@mipmap/icon")]
    public class MainActivity : Activity
    {
        

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.Main);


            Button button1 = FindViewById<Button>(Resource.Id.Activity1LaunchButton);
            button1.Click += delegate { this.StartActivity(new Intent(this, typeof(Activity1))); };

            Button button2 = FindViewById<Button>(Resource.Id.Activity2LaunchButton);
            button2.Click += delegate { this.StartActivity(new Intent(this, typeof(Activity2))); };

            Button button3 = FindViewById<Button>(Resource.Id.Activity3LaunchButton);
            button3.Click += delegate { this.StartActivity(new Intent(this, typeof(Activity3))); };



        }
    }
}

