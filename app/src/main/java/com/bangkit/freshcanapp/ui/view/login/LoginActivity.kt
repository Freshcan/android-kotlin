package com.bangkit.freshcanapp.ui.view.login

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.freshcanapp.R
import com.bangkit.freshcanapp.databinding.ActivityLoginBinding
import com.bangkit.freshcanapp.databinding.ActivityMainBinding
import com.bangkit.freshcanapp.ui.view.NetworkChangeReceiver
import com.bangkit.freshcanapp.ui.view.main.MainActivity
import com.bangkit.freshcanapp.ui.view.register.RegisterActivity
import com.bangkit.freshcanapp.utils.Injection
import com.bangkit.freshcanapp.utils.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var alertDialog: AlertDialog
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(applicationContext, dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this, { user ->
            if(user.isLogin){
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        })
        loginViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        loginViewModel.loginResponse.observe(this, {
            if(it.token.equals("Invalid Email and Password")){
                Log.e("test", "fail")
                AlertDialog.Builder(this).apply {
                    setTitle("Maaf terjadi kesalahan")
                    setMessage(it.token)
                    setPositiveButton("kembali"){ dialog, which ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }

            } else if (it.token.equals("Something went wrong")) {
                Log.e("test", "fail")
                AlertDialog.Builder(this).apply {
                    setTitle("Maaf terjadi kesalahan")
                    setMessage(it.token)
                    setPositiveButton("kembali") { dialog, which ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
        })

    }

    private fun setupAction() {
        binding.signUpButton.setOnClickListener({
            startActivity(Intent(this, RegisterActivity::class.java))
        })
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextError.isVisible = true
                }
                password.isEmpty() -> {
                    binding.passwordEditTextError.isVisible = true
                }
                password.length < 6 -> {

                }
                else -> {
                    binding.emailEditTextError.isVisible = false
                    binding.passwordEditTextError.isVisible = false
                    loginViewModel.login(email, password)
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
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean){
        binding.loginButton.isClickable = !isLoading
        binding.loginButton.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
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