using System;

using Xamarin.Forms;

namespace DataWedgeFormsApp
{
    public class App : Application
    {
       

        public App()
        {
            MainPage = new NavigationPage(new IndexPage());
        }

        protected override void OnStart()
        {
            // Create the DataWedge Proifle if one does not exist
            var dpSrvc = DependencyService.Get<IScanning>();
            dpSrvc.CreateDataWedgeProfile();
        }

        protected override void OnSleep()
        {
            // Handle when your app sleeps
        }

        protected override void OnResume()
        {
            // Handle when your app resumes
        }
    }
}
