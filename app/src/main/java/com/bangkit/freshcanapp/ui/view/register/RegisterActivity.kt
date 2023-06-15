package com.bangkit.freshcanapp.ui.view.register

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.freshcanapp.R
import com.bangkit.freshcanapp.data.remote.request.RegisterRequest
import com.bangkit.freshcanapp.databinding.ActivityMainBinding
import com.bangkit.freshcanapp.databinding.ActivityRegisterBinding
import com.bangkit.freshcanapp.ui.view.NetworkChangeReceiver
import com.bangkit.freshcanapp.ui.view.login.LoginActivity
import com.bangkit.freshcanapp.utils.Injection
import com.bangkit.freshcanapp.utils.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var alertDialog: AlertDialog
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setupNetworkReceiver()
//        setupAllertDialog()
//        checkNetworkAndPopup()
        setupViewModel()
        setupFullScreen()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
//        checkNetworkAndPopup()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
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

    private fun checkNetworkAndPopup() {
        if(!haveNetworkConnection()){
            if(!alertDialog.isShowing){
                alertDialog.show()
            }
        }
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(application, dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.isLoading.observe(this, {
            showLoading(it.get(0))
        })

        registerViewModel.registerResponse.observe(this, {
            if (it != null) {
                if (it.message.equals("Register successed")) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akunnya sudah jadi nih. Yuk, login dan segarkan harimu.")
                        setPositiveButton("Lanjut") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    AlertDialog.Builder(this).apply {
                        setTitle("Maaf terjadi error")
                        setMessage(it.message)
                        setPositiveButton("Kembali", null)
                        create()
                        show()
                    }
                }
            }
        })
        registerViewModel.isLoading.observe(this, {
            showLoading(it.get(0))
        })
    }

    private fun showLoading(isLoading: Boolean){
        binding.registerButton.isClickable = !isLoading
        binding.registerButton.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener({
            finish()
        })
        binding.registerButton.setOnClickListener {
            val name = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confPassword = binding.confirmPasswordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.usernameEditTextError.isVisible = true
                }
                email.isEmpty() -> {
                    binding.emailEditTextError.isVisible = true
                }

                password.isEmpty() -> {
                    binding.passwordEditTextError.isVisible = true
                }
                password.length < 6 -> {

                }
                else -> {
                    binding.usernameEditTextError.isVisible = false
                    binding.emailEditTextError.isVisible = false
                    binding.passwordEditTextError.isVisible = false

                    registerViewModel.register(name, email, password, confPassword)
                }
            }
        }
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