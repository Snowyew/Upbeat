package com.example.hxu.drumset;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.lang.Math;
import android.widget.EditText;
import android.widget.Toast;
import android.media.*;
import android.widget.ToggleButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener, OnClickListener {

    double x, y, z;
    double airP;

    // low pass filter
    private Sensor gyroscope;
    private SensorManager sm;
    double Threshold;

    MediaPlayer crash;
    MediaPlayer snare;
    MediaPlayer tom;
    MediaPlayer rim;
    MediaPlayer hi;
    MediaPlayer bass;
    MediaPlayer beat;

    ToggleButton toggle;
    ToggleButton toggleRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create our sensor manager
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        // gyroscope sensor
        gyroscope = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        Threshold = 5.0;

        if (crash != null) {
            return;
        }
        crash = MediaPlayer.create(getApplicationContext(), R.raw.cymbol);
        snare = MediaPlayer.create(getApplicationContext(), R.raw.snare);
        tom = MediaPlayer.create(getApplicationContext(), R.raw.tom);
        rim = MediaPlayer.create(getApplicationContext(), R.raw.rim);
        hi = MediaPlayer.create(getApplicationContext(), R.raw.hi);
        bass = MediaPlayer.create(getApplicationContext(), R.raw.bass);
        beat = MediaPlayer.create(getApplicationContext(), R.raw.beat);

        new AlertDialog.Builder(this)
            .setTitle("Shake to Play!")
            .setMessage("Click tutorial for more instructions")
            .setIcon(android.R.drawable.star_on)
            .show();

        toggle = (ToggleButton) findViewById(R.id.background);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    beat.setLooping(true);
                    play(beat);
                } else {
                    // The toggle is disabled
                    beat.pause();
                    beat.seekTo(0);
                }
            }
        });
        toggleRec = (ToggleButton) findViewById(R.id.record);
        toggleRec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    startRecording();
                } else {
                    // The toggle is disabled
                    stopRecording();
                }
            }
        });
    }

    protected void onResume() {
        super.onResume();
        // register sensor listener
        if (gyroscope != null) {
            sm.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            Toast.makeText(this, "Your gyroscope is not available", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //TODO Auto-generated method stub
        // not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        Log.d("sensorchanged",event.toString());

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
        }
        Log.d("sensor", "here");
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            airP = event.values[0];
        }

        //Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2))
        double shake = Math.sqrt(x*x+y*y+z*z);

//        textView2.setText("X: " + x + (x>=Threshold ? " (SHAKING)" : "")
//            + "\nY: " + y  + (y>=Threshold ? " (SHAKING)" : "")
//            + "\nZ: " + z  + (z>=Threshold ? " (SHAKING)" : "") );

        if (x>=Threshold) {
            play(snare);
        }
        if (x<=-1) {
            play(rim);
        }
        if (y>=Threshold) {
            play(tom);
        }
        if (y<=-1) {
            play(hi);
        }
        if (z>=Threshold) {
            play(crash);
        }
        if (z<=-1) {
            play(bass);
        }
//        else if (shake < Threshold) {
//            textView4.setText("No Shake!");
//        }
//        else {
//            textView2.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
//            textView4.setText("You Are Shaking!");
//        }

    }

    private void play(MediaPlayer m) {
        if (m.isPlaying()) {
            m.pause();
            m.seekTo(0);
        }
        m.start();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.background:
                play(snare);
                break;
            case R.id.record:
                break;
        }
    }

    public void startRecording(){
        Context context = getApplicationContext();
        CharSequence text = "Recording Started...";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void stopRecording(){
        Context context = getApplicationContext();
        CharSequence text = "Recording Stopped";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void startTutorial(View view){

        Intent intent = new Intent(this, TutorialY.class);
        startActivity(intent);
    }
}
