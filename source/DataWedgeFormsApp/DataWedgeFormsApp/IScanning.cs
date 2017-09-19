using System;
using System.Collections.Generic;

namespace DataWedgeFormsApp
{
    public interface IScanning
    {
        event EventHandler<String> OnData;

        void EnableIntentScanning();
        void DisableIntentScanning();

        void EnableKeystrokeScanning();
        void DisableKeystrokeScanning();


        void CreateDataWedgeProfile();
        void TriggerOnDataEvent(String data);

    }
}
