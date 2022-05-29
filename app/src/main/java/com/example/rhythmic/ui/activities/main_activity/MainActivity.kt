package com.example.rhythmic.ui.activities.main_activity

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.rhythmic.R
import com.example.rhythmic.databinding.ActivityMainBinding
import com.example.rhythmic.domain.util.UIFunctions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

        @Inject
        lateinit var uiFunctions: UIFunctions
        private lateinit var appBarConfiguration: AppBarConfiguration
        private  val mainActivityViewModel: MainActivityViewModel by viewModels()
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
                uiFunctions.setActionBarLogo(activity = this as AppCompatActivity)
//                mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
                mainActivityViewModel.getRuntimePermission(this)
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
                mainActivityViewModel.onRequestPermissionResult(requestCode = requestCode, grantResult = grantResults[0], this)
        }
}