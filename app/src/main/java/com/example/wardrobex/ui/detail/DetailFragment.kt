package com.example.wardrobex.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.wardrobex.databinding.FragmentDetailBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clothingId = arguments?.getString("clothingId") ?: return
        viewModel.loadClothing(clothingId)

        viewModel.clothing.observe(viewLifecycleOwner) { item ->
            if (item == null) return@observe
            
            com.bumptech.glide.Glide.with(this)
                .load(item.imageUrl)
                .into(binding.imgDetail)
                
            if (item.doodleUrl.isNotEmpty()) {
                binding.imgDoodle.visibility = View.VISIBLE
                binding.btnGenerateDoodle.visibility = View.GONE
                com.bumptech.glide.Glide.with(this)
                    .load(item.doodleUrl)
                    .into(binding.imgDoodle)
            } else {
                binding.imgDoodle.visibility = View.GONE
                binding.btnGenerateDoodle.visibility = View.VISIBLE
                binding.btnGenerateDoodle.text = "Convert to Doodle"
            }
                
            binding.tvDetailCategory.text = item.category
            binding.tvDetailColor.text = if (item.color.isNotEmpty()) item.color else "—"
            binding.tvDetailDate.text = SimpleDateFormat(
                "dd MMM yyyy", Locale.getDefault()
            ).format(Date(item.dateAdded))
        }

        viewModel.deleted.observe(viewLifecycleOwner) { deleted ->
            if (deleted == true) {
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
        
        binding.btnGenerateDoodle.setOnClickListener {
            binding.btnGenerateDoodle.text = "Generating..."
            binding.btnGenerateDoodle.isEnabled = false
            viewModel.generateDoodle()
        }

        binding.btnDelete.setOnClickListener {
            viewModel.deleteClothing()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
