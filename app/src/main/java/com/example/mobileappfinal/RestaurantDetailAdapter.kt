package com.example.mobileappfinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappfinal.databinding.ItemRestaurantCardBinding

// RestaurantDetailAdapter.kt
interface OnRestaurantClickListener {
    fun onRestaurantClick(restaurantName: String)
}

class RestaurantDetailAdapter(
    private val restaurantList: List<String>,
    private val listener: OnRestaurantClickListener
) : RecyclerView.Adapter<RestaurantDetailAdapter.RestaurantDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDetailViewHolder {
        val binding = ItemRestaurantCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantDetailViewHolder, position: Int) {
        val restaurantName = restaurantList[position]
        holder.bind(restaurantName)
    }

    override fun getItemCount(): Int = restaurantList.size

    inner class RestaurantDetailViewHolder(private val binding: ItemRestaurantCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurantName: String) {
            binding.restaurantName.text = restaurantName
            binding.root.setOnClickListener {
                listener.onRestaurantClick(restaurantName) // Notify listener when clicked
            }
        }
    }
}
