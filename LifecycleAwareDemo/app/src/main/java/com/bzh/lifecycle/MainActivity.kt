package com.bzh.lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
        Log.d("biezhihua", "connectListener")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun disconnectListener() {
        Log.d("biezhihua", "disconnectListener")
    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runOnUiThread { startActivity(Intent(this, Main2Activity::class.java)) }

        lifecycle.addObserver(MyObserver())
    }

    override fun onStart() {
        super.onStart()
        Log.d("biezhihua", "onStart 1")
    }

    override fun onStop() {
        super.onStop()
        Log.d("biezhihua", "onStop 1")
    }
}
