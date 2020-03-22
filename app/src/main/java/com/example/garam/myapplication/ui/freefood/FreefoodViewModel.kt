package com.example.garam.myapplication.ui.freefood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FreefoodViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        //value = "This is freefood Fragment"
    }
    val text: LiveData<String> = _text
}