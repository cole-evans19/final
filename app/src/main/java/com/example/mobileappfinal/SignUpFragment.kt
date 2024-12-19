package com.example.mobileappfinal

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpFragment : Fragment() {

    private lateinit var nameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var nextButton: Button
    private lateinit var profileImageView: ImageView

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val dialogFragment = ChooseOptionDialogFragment()
            dialogFragment.show(childFragmentManager, "ChooseOptionDialog")
        } else {
            Toast.makeText(requireContext(), "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            photoUri?.let {
                (activity as? SignUpFragment)?.setProfileImage(it)
            }
        }
    }

    private val galleryResultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            (activity as? SignUpFragment)?.setProfileImage(it)
        }
    }

    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInput = view.findViewById(R.id.nameInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        nextButton = view.findViewById(R.id.nextButton)
        profileImageView = view.findViewById(R.id.profilePicture)

        profileImageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                val dialogFragment = ChooseOptionDialogFragment()
                dialogFragment.show(childFragmentManager, "ChooseOptionDialog")
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        nextButton.setOnClickListener {
            val name = nameInput.text.toString()
            val password = passwordInput.text.toString()

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveUserToFirebase(name, password)
        }
    }

    private fun saveUserToFirebase(name: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(name, password)  // Using email for Firebase registration
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    val user = hashMapOf(
                        "name" to name
                    )

                    database.collection("users").document(userId!!).set(user)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "User registered successfully", Toast.LENGTH_SHORT).show()
                            goToHomeScreen()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to save user details", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToHomeScreen() {
        findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
    }

    fun setProfileImage(uri: Uri?) {
        uri?.let {
            profileImageView.setImageURI(it)
        }
    }

    fun setProfileImageFromBitmap(bitmap: Bitmap?) {
        bitmap?.let {
            profileImageView.setImageBitmap(bitmap)
        }
    }
}
