package com.bzh.livedata

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 创建LiveData，并使用
//        val viewModel = NameViewModel()
//
//        viewModel.nameLD.observe(this, Observer {
//            Log.d("biezhihua", "NameViewModel $it")
//        })
//
//        viewModel.nameLD.value = "biezhihua"

        // 2. 自定义LiveData
        val stackLiveData = StockLiveData()
        stackLiveData.observe(this, Observer {
            Log.d("biezhihua", "StockLiveData $it")
        })
    }
}

class NameViewModel : ViewModel() {

    private val name = MutableLiveData<String>()

    val nameLD: MutableLiveData<String> get() = name
}


class StockLiveData() : LiveData<String>() {

    protected override fun onActive() {
        value = "StockLiveData onActive"
    }

    protected override fun onInactive() {
        value = "StockLiveData onInactive"
    }
}

