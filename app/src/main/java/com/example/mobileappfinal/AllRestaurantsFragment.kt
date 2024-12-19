package com.example.mobileappfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappfinal.databinding.FragmentAllRestaurantsBinding

class AllRestaurantsFragment : Fragment(R.layout.fragment_all_restaurants) {

    private var _binding: FragmentAllRestaurantsBinding? = null
    private val binding get() = _binding!!
    private lateinit var restaurantDetailAdapter: RestaurantDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllRestaurantsBinding.bind(view)

        // Sample list of restaurant names for this example
        val restaurantList = listOf("Chipotle", "Chick-Fil-A", "Burger King", "Panera", "McDonald's")

        // Initialize the adapter with the restaurant list and listener
        restaurantDetailAdapter = RestaurantDetailAdapter(restaurantList, object : OnRestaurantClickListener {
            override fun onRestaurantClick(restaurantName: String) {
                // When a card is clicked, navigate to RestaurantDetailFragment and pass the name
                val action = AllRestaurantsFragmentDirections.actionAllRestaurantsFragmentToRestaurantDetailFragment(restaurantName)
                findNavController().navigate(action)
            }
        })

        binding.recyclerViewAll.adapter = restaurantDetailAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

