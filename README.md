# android_examples


# Setup

## Push all three DataWedge Profiles to the DataWedge auto import folder

* adb push dwprofile_DWXamarinActivity1.db /enterprise/device/settings/datawedge/autoimport
* adb push dwprofile_DWXamarinActivity2.db /enterprise/device/settings/datawedge/autoimport
* adb push dwprofile_DWXamarinActivity3.db /enterprise/device/settings/datawedge/autoimport

## Install the APK

* adb install DataWedgeXamarinAndroidApp.apk


#Usage

1. Launch the Demo app
2. Scan a barcode ( observe how barcode data is placed in the entry field)
3. Press the  **Launch Activity two** button
4. Try to scan a Code 128 barcode ( Observe that the Code 128 barcode will not scan)
5. Scan a barcode other than a Code 128 ( Observe that barcodes other than Code 128 will scan)
    * Barcodes that can be scanned in Activty 2 can be veiwed/modified in DataWedge under DWXamarinActivity2 > Decoders
6. Press the  **Launch Activity three** button
7. Try to scan any barcode ( Observe that barcode scanning is completely disabled)






