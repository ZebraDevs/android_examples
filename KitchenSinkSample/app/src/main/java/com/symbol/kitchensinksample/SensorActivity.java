package com.symbol.kitchensinksample;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends Activity implements SensorEventListener
{
    //  Based on https://developer.android.com/guide/topics/sensors/sensors_position.html with fixes

        private SensorManager mSensorManager;
        private final float[] mAccelerometerReading = new float[3];
        private final float[] mMagnetometerReading = new float[3];
        private final float[] mRotationMatrix = new float[9];
        private final float[] mOrientationAngles = new float[3];
        private static String LOG_TAG = "SensorSample";
        private float maximumProximitySensorRange;
        private boolean mCompassSupported = true;
        private Sensor sensor_accelerometer;
        private Sensor sensor_magnetic_field;
        private Sensor sensor_proximity;
        private Sensor sensor_temperature;
        private Sensor sensor_pressure;
        private TextView txtAzmiuth;
        private TextView txtRoll;
        private TextView txtPitch;
        private TextView txtProximity;
        private TextView txtPressure;
        private TextView txtTemperature;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sensor);
        txtAzmiuth = (TextView)findViewById(R.id.valueAzimith);
        txtRoll = (TextView)findViewById(R.id.valueRoll);
        txtPitch = (TextView)findViewById(R.id.valuePitch);
        txtProximity = (TextView)findViewById(R.id.valueProximity);
        txtPressure = (TextView)findViewById(R.id.valuePressure);
        txtTemperature = (TextView)findViewById(R.id.valueTemperature);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        maximumProximitySensorRange = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY).getMaximumRange();
        sensor_accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor_accelerometer == null)
            Log.i(LOG_TAG, "Accelerometer sensor not available on device");
        sensor_magnetic_field = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (sensor_magnetic_field == null)
            Log.i(LOG_TAG, "Magnetic field sensor not available on device");
        if (sensor_accelerometer == null || sensor_magnetic_field == null)
            compassNotSupported();
        sensor_proximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (sensor_proximity == null)
            Log.i(LOG_TAG, "Proximity sensor not available on device");
        sensor_temperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (sensor_temperature == null)
            Log.i(LOG_TAG, "Temperature sensor not available on device");
        sensor_pressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (sensor_pressure == null)
            Log.i(LOG_TAG, "Pressure sensor not available on device");

        //  Find available sensors
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        TextView txtAvailableSensors = (TextView)findViewById(R.id.valueSensors);
        String szAvailableSensors = "";
        for (int i = 0; i < sensorList.size(); i++)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                String sensorName = sensorList.get(i).getStringType() + " (" + sensorList.get(i).getName() + ")";
                Log.i(LOG_TAG, "Sensor: " + sensorName);
                szAvailableSensors += sensorName;
                szAvailableSensors += "\n";
            }
            else
            {
                String sensorName = sensorList.get(i).getName();
                Log.i(LOG_TAG, "Sensor: " + sensorName);
                szAvailableSensors += sensorName;
                szAvailableSensors += "\n";
            }
        }
        txtAvailableSensors.setText(szAvailableSensors);
    }

        @Override
        public void onSensorChanged(SensorEvent event) {
        //  https://developer.android.com/reference/android/hardware/SensorEvent.html#values
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
            updateOrientationAngles();
        }
        else if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
            updateOrientationAngles();
        }
        else if (sensorType == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            if (distance >= maximumProximitySensorRange) {
                Log.d(LOG_TAG, "Proximity: Far");
                txtProximity.setText("Far");
            } else {
                Log.d(LOG_TAG, "Proximity: Near");
                txtProximity.setText("Near");
            }
        }
        else if (sensorType == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];
            Log.d(LOG_TAG, "Temperature (C): " + temperature);
            txtTemperature.setText("" + temperature + " C");
        }
        else if (sensorType == Sensor.TYPE_PRESSURE) {
            float atmospheric_pressure_milibar = event.values[0];
            Log.d(LOG_TAG, "Pressure (hPa): " + atmospheric_pressure_milibar);
            txtPressure.setText("" + atmospheric_pressure_milibar + " hPa");
        }

    }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //  todo
    }

        @Override
        protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        if (sensor_accelerometer != null)
            mSensorManager.registerListener(this, sensor_accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        if (sensor_magnetic_field != null)
            mSensorManager.registerListener(this, sensor_magnetic_field,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        if (sensor_proximity != null)
            mSensorManager.registerListener(this, sensor_proximity,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        if (sensor_temperature != null)
            mSensorManager.registerListener(this, sensor_temperature,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        if (sensor_pressure != null)
            mSensorManager.registerListener(this, sensor_pressure,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

        @Override
        protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        mSensorManager.unregisterListener(this);
    }

        // Compute the three orientation angles based on the most recent readings from
        // the device's accelerometer and magnetometer.
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        mSensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);

        // "mOrientationAngles" now has up-to-date information.
        //Log.d(LOG_TAG, "Orientation Azimuth (X) = " + RadiansToDegrees(mOrientationAngles[0]));
        //Log.d(LOG_TAG, "Orientation Pitch (Y) = " + RadiansToDegrees(mOrientationAngles[1]));
        //Log.d(LOG_TAG, "Orientation Roll (Z) = " + RadiansToDegrees(mOrientationAngles[2]));
        if (mCompassSupported)
        {
            txtAzmiuth.setText("" + RadiansToDegrees(mOrientationAngles[0]));
            txtPitch.setText("" + RadiansToDegrees(mOrientationAngles[1]));
            txtRoll.setText("" + RadiansToDegrees(mOrientationAngles[2]));
        }
    }

    private double RadiansToDegrees(float radians)
    {
        return radians * (180.0f / Math.PI);
    }

    private void compassNotSupported() {
        mCompassSupported = false;
    }
}