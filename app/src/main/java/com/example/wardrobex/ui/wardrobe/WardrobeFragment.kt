package com.example.wardrobex.ui.wardrobe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wardrobex.R
import com.example.wardrobex.databinding.FragmentWardrobeBinding

class WardrobeFragment : Fragment() {

    private var _binding: FragmentWardrobeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WardrobeViewModel by viewModels()
    private lateinit var adapter: ClothingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWardrobeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ClothingAdapter(
            onItemClick = { clothing ->
                // Navigate to detail screen, passing the clothing ID
                val bundle = bundleOf("clothingId" to clothing.id)
                findNavController().navigate(R.id.action_wardrobe_to_detail, bundle)
            },
            onItemLongClick = { clothing ->
                viewModel.delete(clothing)
            }
        )

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        viewModel.allClothing.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.emptyState.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabUpload.setOnClickListener {
            findNavController().navigate(R.id.action_wardrobe_to_upload)
        }

        binding.fabSuggest.setOnClickListener {
            showOccasionInputDialog()
        }

        viewModel.outfitSuggestion.observe(viewLifecycleOwner) { suggestion ->
            if (suggestion != null) {
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Outfit Suggestion")
                    .setMessage(suggestion)
                    .setPositiveButton("Awesome!") { dialog, _ -> 
                        dialog.dismiss() 
                        viewModel.clearSuggestion()
                    }
                    .show()
            }
        }

        viewModel.isSuggesting.observe(viewLifecycleOwner) { isSuggesting ->
            if (isSuggesting == true) {
                binding.fabSuggest.text = "Thinking..."
                binding.fabSuggest.isEnabled = false
            } else {
                binding.fabSuggest.text = "Suggest Outfit"
                binding.fabSuggest.isEnabled = true
            }
        }
    }

    private fun showOccasionInputDialog() {
        val input = android.widget.EditText(requireContext())
        input.hint = "e.g., Casual Date, Office, Gym"
        
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("What's the Occasion?")
            .setView(input)
            .setPositiveButton("Suggest") { _, _ ->
                val occasion = input.text.toString().takeIf { it.isNotBlank() } ?: "Casual day out"
                viewModel.suggestOutfit(occasion)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
