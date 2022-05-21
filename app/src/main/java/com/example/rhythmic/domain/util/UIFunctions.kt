package com.example.rhythmic.domain.util

import androidx.appcompat.app.AppCompatActivity
import com.example.rhythmic.R

class UIFunctions {
        fun setActionBarLogo(activity: AppCompatActivity) {
                (activity).supportActionBar?.apply {
                        setHomeButtonEnabled(true)
                        setDisplayHomeAsUpEnabled(true)
                        setHomeAsUpIndicator(R.drawable.ic_nav_icon)
                }
        }
}