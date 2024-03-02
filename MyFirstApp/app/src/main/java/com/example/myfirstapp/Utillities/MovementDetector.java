package com.example.myfirstapp.Utillities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MovementDetector {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private OnTiltListener onTiltListener;

    private long lastUpdate = 0; // For debouncing
    private static final long DEBOUNCE_TIME = 500; // Debounce time in milliseconds

    // Constructor
    public MovementDetector(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    // Define an interface to handle tilt events
    public interface OnTiltListener {
        void onColumnTilt(int column); // Method to handle column position based on tilt
    }

    // Set the listener
    public void setOnTiltListener(OnTiltListener listener) {
        this.onTiltListener = listener;
    }

    // Sensor event listener
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0]; // Acceleration minus Gx on the x-axis
                int column = calculateColumn(x);
                if (onTiltListener != null) onTiltListener.onColumnTilt(column);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Not used in this context
        }
    };

    private int calculateColumn(float x) {
        if (x < -2) return 4;
        else if (x < -1) return 3;
        else if (x <= 1) return 2;
        else if (x <= 2) return 1;
        else return 0;
    }

    // Method to start listening for sensor events
    public void start() {
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    // Method to stop listening to sensor events
    public void stop() {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
