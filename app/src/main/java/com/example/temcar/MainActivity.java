package com.example.temcar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private TextView txttemcar;
    private static final int FRECUENCIA_MIN = 60;
    private static final int FRECUENCIA_MAX = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txttemcar = findViewById(R.id.txttemcar);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, 1);
        } else {
            startSensor();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        stopSensor();
    }
    private void startSensor() {
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void stopSensor() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float valor = event.values[0];
            //txtValorFrecuenciaCardiaca.setText("Frecuencia Cardiaca:" + valor);
            txttemcar.setText(String.valueOf(valor));
            if (valor < FRECUENCIA_MIN) {
                showHeartRateAlert("Frecuencia Debil, favor de moverse");
            } else if (valor > FRECUENCIA_MAX) {
                showHeartRateAlert("Frecuencia Fuerte, Toma un descanso");
            } else {
                showHeartRateAlert("Frecuencia cardíaca normal");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    private void showHeartRateAlert(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

