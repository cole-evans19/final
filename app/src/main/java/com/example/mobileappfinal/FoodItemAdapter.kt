package com.example.mobileappfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappfinal.databinding.ItemFoodBinding

class FoodItemAdapter(
    private val foodItems: List<String>, // List of food items, e.g. "Chicken: $8.50"
    private val initialCounts: MutableMap<String, Int> // Mutable map to hold counts
) : RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val foodItem = foodItems[position]
        val foodName = foodItem.split(":")[0]
        holder.foodName.text = foodName

        // Set the initial count
        holder.itemCount.text = initialCounts[foodName]?.toString() ?: "0"

        // Decrement button logic
        holder.minusButton.setOnClickListener {
            var count = initialCounts[foodName] ?: 0
            if (count > 0) {
                count--
                initialCounts[foodName] = count
                holder.itemCount.text = count.toString()
            }
        }

        // Increment button logic
        holder.plusButton.setOnClickListener {
            var count = initialCounts[foodName] ?: 0
            count++
            initialCounts[foodName] = count
            holder.itemCount.text = count.toString()
        }
    }

    override fun getItemCount(): Int = foodItems.size

    // ViewHolder class to bind the view components
    inner class FoodItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.foodItem)
        val minusButton: Button = itemView.findViewById(R.id.minusButton)
        val itemCount: TextView = itemView.findViewById(R.id.itemCount)
        val plusButton: Button = itemView.findViewById(R.id.plusButton)
    }
}
