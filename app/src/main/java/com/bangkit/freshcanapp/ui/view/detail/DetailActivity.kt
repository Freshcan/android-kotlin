package com.bangkit.freshcanapp.ui.view.detail

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.freshcanapp.R
import com.bangkit.freshcanapp.data.remote.response.SpecificHistoryResponse
import com.bangkit.freshcanapp.databinding.ActivityDetailBinding
import com.bangkit.freshcanapp.ui.view.main.MainViewModel
import com.bangkit.freshcanapp.utils.Injection
import com.bangkit.freshcanapp.utils.ViewModelFactory
import com.bumptech.glide.Glide
import java.io.File
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var id: String
    private lateinit var informationName: String
    private lateinit var previousPage: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setupFullScreen()
        previousPage = intent.getStringExtra("previousPage")!!
        if(previousPage.equals("History")){
            getExtraHistory()
            setupViewModelHistory()
        }else if(previousPage.equals("Home")){
            getFileFromExtra()
            setupViewModelHome()
        }
        setupNotFoundAction()
    }

    private fun setupNotFoundAction() {
        binding.goBackHomeButton.setOnClickListener({
            finish()
        })
    }

    private fun getFileFromExtra() {
        val currentPhotoPath = intent.getStringExtra("currentPhotoPath")
        val myFile = File(currentPhotoPath)
        getFile = myFile
    }

    private fun setupViewModelHome() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(applicationContext, dataStore))
        )[MainViewModel::class.java]

        mainViewModel.uploadImage(getFile!!)

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.specificHistoryResponse.observe(this, {
            setupData(it!!)
        })

        mainViewModel.uploadImageResponse.observe(this, {
            if(it.statusCode != 200){
                showNotFound()
            }
        })
    }

    private fun getExtraHistory() {
        id = intent.getStringExtra("id")!!
        informationName = intent.getStringExtra("informationName")!!
    }

    private fun setupViewModelHistory() {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(applicationContext, dataStore))
        )[DetailViewModel::class.java]

        detailViewModel.getSpecificHistory(id, informationName)

        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        detailViewModel.specificHistoryResponse.observe(this, {
            setupData(it!!)
        })
    }

    private fun setupData(it: SpecificHistoryResponse) {
        if(it.statusCode == 200) {
            val condition = it.payload?.queryHistory?.get(0)?.condition

            Glide.with(applicationContext)
                .load(it.payload?.queryHistory?.get(0)?.urlImage) // URL Gambar
                .into(binding.pictureContent)

            val sourceString  = "<b>" + it.payload?.queryInformation?.get(0)?.botanicalName + "</b> " + ", " + it.payload?.queryInformation?.get(0)?.description;
            binding.descriptionContentText.setText(Html.fromHtml(sourceString))
            binding.benefitsContentText.text = it.payload?.queryInformation?.get(0)?.benefit!!
            binding.funfactContentText.text = it.payload?.queryInformation?.get(0)?.funfact

            binding.nameContentText.text = it.payload?.queryHistory?.get(0)?.informationName
            binding.textPercentage.text = it.payload?.queryHistory?.get(0)?.percentage
            if(condition.equals("Rotten")){
                val alphaPercentage =
                    it.payload?.queryHistory?.get(0)?.percentage!!.split(".")[0].toInt();
                val color = ContextCompat.getColor(applicationContext, R.color.rotten_freshcan)
                val alphaColor = ColorUtils.setAlphaComponent(color, (alphaPercentage * 255) / 100)// Replace with your hexadecimal color string

                binding.circlePercentage.setCardBackgroundColor(alphaColor)
                binding.freshnessContentText.text = condition
                binding.freshnessContentText.setTextColor(ContextCompat.getColor(applicationContext, R.color.rotten_freshcan))
            }else{
                val alphaPercentage =
                    it.payload?.queryHistory?.get(0)?.percentage!!.split(".")[0].toInt();
                val color = ContextCompat.getColor(applicationContext, R.color.green_freshcan)
                val alphaColor = ColorUtils.setAlphaComponent(color, (alphaPercentage * 255) / 100)// Replace with your hexadecimal color string

                binding.circlePercentage.setCardBackgroundColor(alphaColor)
                binding.freshnessContentText.text = condition
                binding.freshnessContentText.setTextColor(ContextCompat.getColor(applicationContext, R.color.green_freshcan))
            }



            binding.allergyContentText.text = it.payload?.queryInformation?.get(0)?.allergy
            binding.energyContentText.text = it.payload?.queryInformation?.get(0)?.energy
            binding.waterContentText.text = it.payload?.queryInformation?.get(0)?.water
            binding.proteinContentText.text = it.payload?.queryInformation?.get(0)?.protein
            binding.totalfatContentText.text = it.payload?.queryInformation?.get(0)?.totalFat
            binding.carbohydaratesContentText.text = it.payload?.queryInformation?.get(0)?.carbohydrates
            binding.fiberContentText.text = it.payload?.queryInformation?.get(0)?.fiber
            binding.sugarContentText.text = it.payload?.queryInformation?.get(0)?.sugars
            binding.calsiumContentText.text = it.payload?.queryInformation?.get(0)?.calsium
            binding.ironContentText.text = it.payload?.queryInformation?.get(0)?.iron
        }else{
            showNotFound()
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
        binding.detailPage.isVisible = !isLoading
        binding.loadingPage.isVisible = isLoading
    }

    private fun showNotFound(){
        binding.detailPage.isVisible = false
        binding.notFoundPage.isVisible = true
    }
}