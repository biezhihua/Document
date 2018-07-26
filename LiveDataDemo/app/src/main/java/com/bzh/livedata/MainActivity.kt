package com.bzh.livedata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

class MainActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = NameViewModel()
        viewModel.nameLD.observe(this, Observer { })
    }
}

class NameViewModel : ViewModel() {

    private val name = MutableLiveData<String>()

    val nameLD: MutableLiveData<String> get() = name
}
