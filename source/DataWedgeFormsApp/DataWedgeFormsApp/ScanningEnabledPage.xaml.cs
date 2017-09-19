using System;
using System.Collections.Generic;

using Xamarin.Forms;

namespace DataWedgeFormsApp
{
    public partial class ScanningEnabledPage : ContentPage
    {

        public ScanningEnabledPage()
        {
            InitializeComponent();
            this.Title = "DataWedgeFormsApp";

            // Enable Intent scanning for this Form
			var dpSrvc = DependencyService.Get<IScanning>();
            dpSrvc.EnableIntentScanning();

            //Attach and event handler for OnData
			dpSrvc.OnData += (App, data) =>
			{
				barcodeEnabledEntry.Text = data;
			};
        }


    }
}
