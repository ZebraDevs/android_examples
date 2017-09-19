using System;
using System.Collections.Generic;

using Xamarin.Forms;

namespace DataWedgeFormsApp
{
    public partial class ScanningDisabledPage : ContentPage
    {
        public ScanningDisabledPage()
        {
            InitializeComponent();
            this.Title = "DataWedgeFormsApp";

            //Disable Intent scanning for this form
			var dpSrvc = DependencyService.Get<IScanning>();
			dpSrvc.DisableIntentScanning();

        }
    }
}
