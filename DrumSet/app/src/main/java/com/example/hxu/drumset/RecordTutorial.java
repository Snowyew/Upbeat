package com.example.hxu.drumset;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RecordTutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_tutorial);
    }

    public void nextTut(View view){
        Intent intent = new Intent(this, Beat.class);
        startActivity(intent);
    }
}
