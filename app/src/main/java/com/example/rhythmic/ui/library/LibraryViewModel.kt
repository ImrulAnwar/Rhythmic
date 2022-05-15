package com.example.rhythmic.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LibraryViewModel : ViewModel() {

        private val _text = MutableLiveData<String>().apply {
                value = "This is slideshow Fragment"
        }
        val text: LiveData<String> = _text
}