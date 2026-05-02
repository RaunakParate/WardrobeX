package com.example.wardrobex.ui.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.wardrobex.R
import com.example.wardrobex.databinding.FragmentUploadBinding
import com.example.wardrobex.utils.FileUtils
import java.io.File

class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UploadViewModel by viewModels()

    // Temp file used when capturing from camera
    private var cameraImageFile: File? = null

    // ── Activity Result Launchers ──────────────────────────────────────────────

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    viewModel.onImageSelected(uri)
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cameraImageFile?.let { file ->
                    viewModel.onImageSelected(Uri.fromFile(file))
                }
            }
        }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
            if (grants.values.all { it }) openGallery()
            else Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategorySpinner()
        observeViewModel()

        binding.btnGallery.setOnClickListener { checkPermissionsAndOpenGallery() }
        binding.btnCamera.setOnClickListener { openCamera() }
        binding.btnSave.setOnClickListener { saveClothing() }
    }

    // ── Setup ─────────────────────────────────────────────────────────────────

    private fun setupCategorySpinner() {
        val categories = listOf("Top", "Bottom", "Shoes", "Accessory", "Other")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.selectedImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.imgPreview.load(uri) { crossfade(true) }
                binding.tvPlaceholder.visibility = View.GONE
            }
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
                viewModel.clearSelection()
                findNavController().navigateUp()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
        
        viewModel.isUploading.observe(viewLifecycleOwner) { uploading ->
            binding.btnSave.isEnabled = !uploading
            binding.btnSave.text = if (uploading) "Uploading..." else "Save"
        }
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    private fun checkPermissionsAndOpenGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            permissionLauncher.launch(arrayOf(permission))
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openCamera() {
        val (uri, file) = FileUtils.createImageUri(requireContext())
        cameraImageFile = file
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        cameraLauncher.launch(intent)
    }

    private fun saveClothing() {
        val category = binding.spinnerCategory.selectedItem as? String ?: "Other"
        val color = binding.etColor.text.toString().trim()
        viewModel.saveClothing(category, color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
