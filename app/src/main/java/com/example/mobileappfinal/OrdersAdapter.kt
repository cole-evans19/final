package com.example.mobileappfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappfinal.databinding.ItemOrderBinding
import com.example.mobileappfinal.model.Order

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    private val ordersList = mutableListOf<Order>()

    fun submitList(newOrders: List<Order>) {
        ordersList.clear()
        ordersList.addAll(newOrders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(ordersList[position])
    }

    override fun getItemCount(): Int = ordersList.size

    class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvRestaurant.text = order.restaurant
            binding.tvFood.text = order.food
            binding.tvAddress.text = order.address
            binding.tvDateTime.text = order.dateTime
            binding.tvPrice.text = "Price: $${order.price}"
            binding.tvQuantity.text = "Quantity: ${order.quantity}"
        }
    }
}
