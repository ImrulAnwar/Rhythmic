package com.example.rhythmic.ui.activities.main_activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.rhythmic.R
import com.example.rhythmic.databinding.ActivityMainBinding
import com.example.rhythmic.domain.util.UIFunctions
import com.example.rhythmic.services.MusicService
import com.example.rhythmic.ui.activities.now_playing_activity.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.vertical_item.*
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

        @Inject
        lateinit var uiFunctions: UIFunctions
        private lateinit var appBarConfiguration: AppBarConfiguration
        private val mainActivityViewModel: MainActivityViewModel by viewModels()
        private lateinit var binding: ActivityMainBinding


        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

                setSupportActionBar(binding.appBarMain.toolbar)
                val drawerLayout: DrawerLayout = binding.drawerLayout
                val navView: NavigationView = binding.navView
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                appBarConfiguration = AppBarConfiguration(
                        setOf(
                                R.id.nav_home, R.id.nav_search, R.id.nav_library, R.id.nav_download
                        ), drawerLayout
                )
                setupActionBarWithNavController(navController, appBarConfiguration)

                bottomNavigationView.setupWithNavController(navController)
                navView.setupWithNavController(navController)
//                nowPlayingViewModel = ViewModelProvider(this)[NowPlayingViewModel::class.java]
                uiFunctions.setActionBarLogo(activity = this as AppCompatActivity)
                mainActivityViewModel.getRuntimePermission(this)
                mainActivityViewModel.getCurrentSong().observe(this) {
                        tvBtmTbrSongTitle.text = it.title
                        tvBtmTbrArtistName.text = it.artist
                        Glide.with(this).load(it.imagePath)
                                .placeholder(R.mipmap.ic_launcher).centerCrop()
                                .into(ivBtmTbrAlbumArt)
                }

                mainActivityViewModel.getCurrentSong().observe(this) {

                        val playPauseButton: Int = if (mainActivityViewModel.isPlaying().value ==true) R.drawable.ic_pause else R.drawable.ic_play
                        val likeButton: Int = if (it.isLiked) R.drawable.ic_loved else R.drawable.ic_love

                        mainActivityViewModel.getBitmapAndShowNotification(it, this, intent, playPauseButton= playPauseButton, likeButton = likeButton)
                }

                mainActivityViewModel.isPlaying().observe(this){
                        val playPauseButton: Int = if (mainActivityViewModel.isPlaying().value ==true) R.drawable.ic_pause else R.drawable.ic_play
                        ibBtmTbrPlayPause.setImageResource(playPauseButton)
                        mainActivityViewModel.getCurrentSong().value?.let {
                                val likeButton: Int = if (it.isLiked) R.drawable.ic_loved else R.drawable.ic_love
                                mainActivityViewModel.getBitmapAndShowNotification(it, this, intent, playPauseButton= playPauseButton, likeButton = likeButton)
                        }
                }

                ibBtmTbrPlayPause.setOnClickListener {

                }
        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
                // Inflate the menu; this adds items to the action bar if it is present.
                menuInflater.inflate(R.menu.main, menu)
                return true
        }

        override fun onSupportNavigateUp(): Boolean {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        }

        override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<out String>,
                grantResults: IntArray
        ) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                mainActivityViewModel.onRequestPermissionResult(
                        requestCode = requestCode,
                        grantResult = grantResults[0],
                        this
                )
        }

}