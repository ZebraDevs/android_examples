using System;
using System.Collections.Generic;

using Xamarin.Forms;

namespace DataWedgeFormsApp
{
    public partial class IndexPage : ContentPage
    {
        public IndexPage()
        {
            InitializeComponent();
            this.Title = "DataWedgeFormsApp";
        }

		void OnScanEnabledFormBtnClick(object sender, System.EventArgs e)
		{
            this.Navigation.PushAsync(new ScanningEnabledPage());
		}

		void OnScanDisabledFormBtnClick(object sender, System.EventArgs e)
		{
			this.Navigation.PushAsync(new ScanningDisabledPage());
		}

		void OnScanEnableDisableFieldBtnClick(object sender, System.EventArgs e)
		{
			this.Navigation.PushAsync(new ScanningEnableDisableFieldPage());
		}

		void OnKeyStrokeInputBtnClick(object sender, System.EventArgs e)
		{
			this.Navigation.PushAsync(new KeystrokeInputPage());
		}
	}
}
