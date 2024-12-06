package com.example.slmabookfinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class SharedViewModel : ViewModel() {

    // MutableLiveData to store the slambook data
    val slambookData = MutableLiveData<SlambookEntry?>()

    // Method to set the slambook data
    fun setSlambookData(slambook: SlambookEntry?) {
        slambookData.value = slambook
    }
}
