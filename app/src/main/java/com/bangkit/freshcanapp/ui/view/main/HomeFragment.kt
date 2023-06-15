package com.bangkit.freshcanapp.ui.view.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.freshcanapp.data.local.preferences.UserModel
import com.bangkit.freshcanapp.databinding.FragmentHomeBinding
import com.bangkit.freshcanapp.ui.view.detail.DetailActivity
import com.bangkit.freshcanapp.ui.view.login.LoginActivity
import com.bangkit.freshcanapp.utils.Injection
import com.bangkit.freshcanapp.utils.ViewModelFactory
import com.bangkit.freshcanapp.utils.createCustomTempFile
import com.bangkit.freshcanapp.utils.uriToFile
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var user: UserModel
    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {

//                getFile = file
//                getFile?.absolutePath
                val intentDetail = Intent(requireActivity(), DetailActivity::class.java)
                intentDetail.putExtra("currentPhotoPath", currentPhotoPath)
                intentDetail.putExtra("previousPage", "Home")
                startActivity(intentDetail)
//                mainViewModel.uploadImage(getFile!!)
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireActivity())
                val intentDetail = Intent(requireActivity(), DetailActivity::class.java)
                intentDetail.putExtra("currentPhotoPath", myFile.absolutePath)
                intentDetail.putExtra("previousPage", "Home")
                startActivity(intentDetail)
//                mainViewModel.uploadImage(getFile!!)
            }
        }
    }
    private fun allPermissionsGranted() = HomeFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(activity?.baseContext!!, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        startHeartbeatAnimation()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        startHeartbeatAnimation()
        Log.e("HomeFragment", user.toString())
    }



    private fun setupAction() {
        binding.frameLayout.setOnClickListener { }
        binding.frameLayout.setOnTouchListener { _, event -> onFrameLayoutTouch(event) }
        binding.logoutLayout.setOnClickListener({
            mainViewModel.logout()
        })
        binding.frameLayoutGallery.setOnClickListener {
            startGallery()
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(requireActivity().applicationContext, requireActivity().dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(viewLifecycleOwner, { user ->
            this.user = user
        })

    }

    private fun toLoginTest() {
        Log.e("MainActivity", "not login")
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun startHeartbeatAnimation() {
        HeartbeatAnimation.startHeartbeatAnimation(binding.frameLayout)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopHeartbeatAnimation()
    }

    private fun stopHeartbeatAnimation() {
        HeartbeatAnimation.stopHeartbeatAnimation(binding.frameLayout)
    }

    private fun onFrameLayoutTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.e("animationButtonFreshcan", "down")
                HeartbeatAnimation.scaleDownAnimation(binding.frameLayout)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.e("animationButtonFreshcan", "up")
                HeartbeatAnimation.stopHeartbeatAnimation(binding.frameLayout)
                HeartbeatAnimation.startHeartbeatAnimation(binding.frameLayout)
                startTakePhoto()
            }
        }
        return false
    }


    private fun startTakePhoto() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                HomeFragment.REQUIRED_PERMISSIONS,
                HomeFragment.REQUEST_CODE_PERMISSIONS
            )
        }else{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(requireActivity().packageManager)

            createCustomTempFile(requireActivity().application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireActivity(),
                    "com.bangkit.freshcanapp",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == HomeFragment.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startTakePhoto()
            } else {
                Toast.makeText(requireActivity(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

object HeartbeatAnimation{
    fun startHeartbeatAnimation(view: View) {
        val scaleAnimation = ScaleAnimation(
            1f, 1.08f, // Start and end scale X
            1f, 1.08f, // Start and end scale Y
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X (center of the view)
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot Y (center of the view)
        ).apply {
            duration = 1200 // Duration of the animation in milliseconds
            repeatCount = Animation.INFINITE // Repeat the animation indefinitely
            repeatMode = Animation.REVERSE // Reverse the animation on each repetition
        }
        view.startAnimation(scaleAnimation)
    }

    fun scaleDownAnimation(view: View) {
        val scaleDownAnimation = ScaleAnimation(
            view.scaleX, 0.85f, // Start and end scale X
            view.scaleY, 0.85f, // Start and end scale Y
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X (center of the view)
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot Y (center of the view)
        ).apply {
            duration = 700 // Duration of the animation in milliseconds
            fillAfter = true // Keep the final scale after the animation ends
        }
        view.startAnimation(scaleDownAnimation)
    }

    fun stopHeartbeatAnimation(view: View) {
        view.clearAnimation()
    }
}