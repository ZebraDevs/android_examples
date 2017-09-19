using System;
using System.Collections.Generic;

using Xamarin.Forms;

namespace DataWedgeFormsApp
{
    public partial class KeystrokeInputPage : ContentPage
    {
        public KeystrokeInputPage()
        {
            InitializeComponent();

            this.Title = "DataWedgeFormsApp";

            // Enable Keystroke scanning when this page instance is created
			var dpSrvc = DependencyService.Get<IScanning>();
            dpSrvc.EnableKeystrokeScanning();
        }
    }
}
