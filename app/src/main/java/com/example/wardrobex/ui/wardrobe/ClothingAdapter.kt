package com.example.wardrobex.ui.wardrobe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wardrobex.databinding.ItemClothingBinding
import com.example.wardrobex.model.Clothing

class ClothingAdapter(
    private val onItemClick: (Clothing) -> Unit,
    private val onItemLongClick: (Clothing) -> Unit
) : ListAdapter<Clothing, ClothingAdapter.ClothingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val binding = ItemClothingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ClothingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ClothingViewHolder(
        private val binding: ItemClothingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clothing: Clothing) {
            Glide.with(binding.root.context)
                .load(clothing.imageUrl)
                .centerCrop()
                .into(binding.imgClothing)
                
            binding.tvCategory.text = clothing.category

            binding.root.setOnClickListener { onItemClick(clothing) }
            binding.root.setOnLongClickListener {
                onItemLongClick(clothing)
                true
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Clothing>() {
            override fun areItemsTheSame(oldItem: Clothing, newItem: Clothing) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Clothing, newItem: Clothing) =
                oldItem == newItem
        }
    }
}
