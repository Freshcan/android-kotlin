package com.bangkit.freshcanapp.ui.view.main

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.freshcanapp.R
import com.bangkit.freshcanapp.data.local.preferences.UserModel
import com.bangkit.freshcanapp.databinding.ActivityMainBinding
import com.bangkit.freshcanapp.databinding.TabTitleBinding
import com.bangkit.freshcanapp.ui.view.NetworkChangeReceiver
import com.bangkit.freshcanapp.ui.view.SectionsPagerAdapter
import com.bangkit.freshcanapp.ui.view.login.LoginActivity
import com.bangkit.freshcanapp.utils.Injection
import com.bangkit.freshcanapp.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var user: UserModel
    private lateinit var alertDialog: AlertDialog
    private lateinit var networkChangeReceiver: NetworkChangeReceiver


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        setupNetworkReceiver()
//        setupAllertDialog()
//        checkNetworkAndPopup()
        setupViewModel()
        setupFullScreen()
        setupViewPager()

    }

    private fun setupNetworkReceiver() {
        networkChangeReceiver = NetworkChangeReceiver()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    private fun setupAllertDialog() {
        alertDialog = AlertDialog.Builder(this).let {
            it.setTitle("Warning")
            it.setMessage("Please connect your phone to the internet")
            it.setPositiveButton("close") { dialog, which ->
                dialog.dismiss()
            }.create()
        }
    }

    override fun onResume() {
        super.onResume()
//        checkNetworkAndPopup()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    private fun checkNetworkAndPopup() {
        if(!haveNetworkConnection()){
            if(!alertDialog.isShowing){
                alertDialog.show()
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(applicationContext, dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this, { user ->
            if (user.isLogin){
                Log.e("MainActivity", "login")
                this.user = user
//                checkNetworkAndPopup()
            } else {
                Log.e("MainActivity", "not login")
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        })
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this@MainActivity)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        viewPager.currentItem = 1
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->

        }.attach()

        val bindingMenuItem = TabTitleBinding.inflate(LayoutInflater.from(applicationContext))
        bindingMenuItem.imageViewMenuIcon.setImageResource(R.drawable.ic_baseline_history_24)
        val frameLayout1 = bindingMenuItem.root
        tabs.getTabAt(0)?.customView = frameLayout1

        val bindingMenuItem2 = TabTitleBinding.inflate(LayoutInflater.from(applicationContext))
        bindingMenuItem2.imageViewMenuIcon.setImageResource(R.drawable.ic_baseline_home_24)
        val frameLayout2 = bindingMenuItem2.root
        tabs.getTabAt(1)?.customView = frameLayout2
//
//        val frameLayout3 = LayoutInflater.from(applicationContext).inflate(R.layout.tab_title, null) as FrameLayout
//        tabs.getTabAt(2)?.customView = frameLayout3
    }

    private fun setupFullScreen() {
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        supportActionBar?.hide()
    }

    private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI",
                    ignoreCase = true)
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE",
                    ignoreCase = true)
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

}

