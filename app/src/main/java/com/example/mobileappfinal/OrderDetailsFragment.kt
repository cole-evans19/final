package com.example.mobileappfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobileappfinal.databinding.FragmentOrderDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve data from the arguments
        val orderedItems = arguments?.getSerializable("ordered_items") as? ArrayList<Pair<String, Int>>
        val restaurantName = arguments?.getString("restaurant_name")
        val address = arguments?.getString("address")
        val specialInstructions = arguments?.getString("special_instructions")
        val orderTime = arguments?.getString("order_time")

        // Display the order details
        binding.restaurantName.text = "Ordered From: $restaurantName"
        binding.orderTime.text = "Order Time: $orderTime"
        binding.deliveryAddress.text = "Delivery Address: $address"
        binding.specialInstructions.text = "Special Instructions: $specialInstructions"

        // Format the ordered items into a string
        val orderedItemsText = orderedItems?.joinToString(separator = "\n") { "${it.first} x${it.second}" }
        binding.orderedItems.text = "Items Ordered:\n$orderedItemsText"

        binding.trackOrder.setOnClickListener {

            findNavController().navigate(R.id.orderToTrack)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
