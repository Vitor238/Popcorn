package com.vitor238.popcorn.ui.preferences.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivityEditProfileBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : BaseActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(
            toolbar = binding.toolbar,
            showBackButton = true,
            titleIdRes = R.string.edit_profile
        )
        setupGalleryResult()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ProfilePreferences())
            .commit()

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.getFirestoreUser()
        profileViewModel.firestoreUserLiveData.observe(this) { user ->

            Glide.with(this)
                .load(user?.photoUrl)
                .circleCrop()
                .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_account_circle_24))
                .into(binding.imageProfile)
        }

        profileViewModel.newPhotoUrlMutableLiveData.observe(this) { newPhotoUrl ->
            newPhotoUrl?.let {
                profileViewModel.updateFirestorePhoto(it)
                profileViewModel.updateAuthPhoto(it)
            }
        }

        binding.imageChangePhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    openGallery()
                }
            }
        }
    }

    private fun setupGalleryResult() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { imageUri ->
                        val inputStream = contentResolver.openInputStream(imageUri)
                        if (inputStream != null) {
                            profileViewModel.uploadNewProfilePicture(inputStream)
                        }
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    companion object {
        private const val PERMISSION_CODE = 10
    }
}