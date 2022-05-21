package com.example.rhythmic.ui.main_activity.fragments.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rhythmic.data.entities.Song

class HomeViewModel : ViewModel() {

        private val _text = MutableLiveData<String>().apply {
                value = "This is home Fragment"
        }
        val text: LiveData<String> = _text
//        fun insertAllSongs(context: Context) : LiveData<List<Song>> {
//
//        }
}