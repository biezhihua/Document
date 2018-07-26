package com.bzh.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called 2");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called 2");
    }

    private static final String TAG = "biezhihua";
}
