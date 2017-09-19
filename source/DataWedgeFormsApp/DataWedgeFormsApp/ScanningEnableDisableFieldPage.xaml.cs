using System;
using System.Collections.Generic;

using Xamarin.Forms;

namespace DataWedgeFormsApp
{
    public partial class ScanningEnableDisableFieldPage : ContentPage
    {
        

        public ScanningEnableDisableFieldPage()
        {
            InitializeComponent();
            this.Title = "DataWedgeFormsApp";

			var dpSrvc = DependencyService.Get<IScanning>();

            //Enable Intent scanning when this entry field is selected
			barcodeEnabledEntry.Focused += (App,data) =>
			{
                dpSrvc.EnableIntentScanning();
			};

            //Disable Intent scanning when this entry field is selected
			barcodeDisabledEntry.Focused += (App, data) =>
			{
				dpSrvc.DisableIntentScanning();
			};

            //Attach event handler for OnData
			dpSrvc.OnData += (App, data) =>
			{
                barcodeEnabledEntry.Text = data;
			};
                               
        }
    }
}
