package com.example.rhythmic.ui.main_activity.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.rhythmic.domain.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        private val repository: Repository,
        application: Application
)  : AndroidViewModel(application) {


}