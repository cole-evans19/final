package com.example.mobileappfinal

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment

class ChooseOptionDialogFragment : DialogFragment() {

    // Launchers for handling gallery and camera intents
    private val galleryResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            (activity as? SignUpFragment)?.setProfileImage(it)
        }
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            photoUri?.let {
                (activity as? SignUpFragment)?.setProfileImage(it)
            }
        }
    }

    private var photoUri: Uri? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_choose_option, null)

        builder.setView(view)

        val galleryButton = view.findViewById<Button>(R.id.galleryButton)
        val cameraButton = view.findViewById<Button>(R.id.cameraButton)

        // Gallery button opens the gallery and returns the image URI
        galleryButton.setOnClickListener {
            galleryResultLauncher.launch("image/*")  // Launches the gallery
            dismiss()
        }

        // Camera button captures a photo and returns the image URI
        cameraButton.setOnClickListener {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, "Profile Picture")
            }
            photoUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            cameraResultLauncher.launch(photoUri)  // Launches the camera
        }

        return builder.create()
    }
}
