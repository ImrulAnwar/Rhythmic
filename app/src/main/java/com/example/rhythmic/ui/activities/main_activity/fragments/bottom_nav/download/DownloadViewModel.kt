package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DownloadViewModel : ViewModel() {
        private val _text = MutableLiveData<String>().apply {
                value = "This is download Fragment"
        }
        val text: LiveData<String> = _text
}