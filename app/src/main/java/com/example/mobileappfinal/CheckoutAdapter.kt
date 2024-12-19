package com.example.mobileappfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CheckoutAdapter(
    private val orderedFoodItems: List<Pair<String, Int>> // Pair of food item and quantity
) : RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return CheckoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val (foodName, quantity) = orderedFoodItems[position]
        holder.foodName.text = foodName
        holder.quantity.text = "Quantity: $quantity"
    }

    override fun getItemCount(): Int = orderedFoodItems.size

    inner class CheckoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.foodItem)
        val quantity: TextView = itemView.findViewById(R.id.itemCount)
    }
}
