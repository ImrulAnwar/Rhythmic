package com.example.rhythmic.ui.main_activity.fragments.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DownloadViewModel : ViewModel() {
        private val _text = MutableLiveData<String>().apply {
                value = "This is download Fragment"
        }
        val text: LiveData<String> = _text
}