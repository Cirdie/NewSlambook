package com.example.slmabookfinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class SharedViewModel : ViewModel() {

    val slambookData = MutableLiveData<SlambookEntry?>()

    fun setSlambookData(slambook: SlambookEntry?) {
        slambookData.value = slambook
    }
}
