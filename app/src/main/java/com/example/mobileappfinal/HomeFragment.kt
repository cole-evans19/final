package com.example.mobileappfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappfinal.databinding.HomeFragmentBinding
import com.example.mobileappfinal.databinding.ItemRestaurantCardBinding
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment(R.layout.home_fragment), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    // Sample data for favorites and all restaurants
    private val favoriteRestaurants = listOf(
        "Chipotle", "Chick-Fil-A", "Burger King"
    )
    private val allRestaurants = listOf(
        "Panera", "McDonald's", "Z and C", "Taco Bell", "Chili's"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = HomeFragmentBinding.bind(view)

        // Set up toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = ""

        // Configure drawer toggle
        val drawerToggle = androidx.appcompat.app.ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Set navigation item listener
        binding.navView.setNavigationItemSelectedListener(this)

        // Set up RecyclerView for favorites
        val favoritesAdapter = RestaurantAdapter(favoriteRestaurants)
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.favoritesRecyclerView.adapter = favoritesAdapter

        // Set up RecyclerView for all restaurants
        val allRestaurantsAdapter = RestaurantAdapter(allRestaurants)
        binding.allRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.allRecyclerView.adapter = allRestaurantsAdapter
    }

    // Adapter for displaying restaurant cards
    inner class RestaurantAdapter(private val restaurants: List<String>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
            val binding = ItemRestaurantCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return RestaurantViewHolder(binding)
        }

        override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
            val restaurantName = restaurants[position]
            holder.bind(restaurantName)
        }

        override fun getItemCount(): Int = restaurants.size

        inner class RestaurantViewHolder(private val binding: ItemRestaurantCardBinding) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

            fun bind(restaurantName: String) {
                binding.restaurantName.text = restaurantName

                // When the card is clicked, pass the restaurant name to RestaurantDetailFragment
                binding.root.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToRestaurantDetailFragment(restaurantName)
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_recent_orders -> findNavController().navigate(R.id.hometorecents)
            R.id.nav_calendar -> findNavController().navigate(R.id.action_homeFragment_to_calendarViewFragment)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
