package com.example.mobileappfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappfinal.databinding.FragmentAllRestaurantsBinding
import com.example.mobileappfinal.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var restaurantDetailAdapter: RestaurantDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)

        // Sample list of favorite restaurant names for this example
        val favoriteRestaurantList = listOf("Taco Bell", "Chili's", "McDonald's")

        // Initialize the adapter with the favorite restaurant list and listener
        restaurantDetailAdapter = RestaurantDetailAdapter(favoriteRestaurantList, object : OnRestaurantClickListener {
            override fun onRestaurantClick(restaurantName: String) {
                // When a card is clicked, navigate to RestaurantDetailFragment and pass the name
                val action = FavoritesFragmentDirections.actionFavoritesFragmentToRestaurantDetailFragment(restaurantName)
                findNavController().navigate(action)
            }
        })

        binding.recyclerViewFavorites.adapter = restaurantDetailAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
