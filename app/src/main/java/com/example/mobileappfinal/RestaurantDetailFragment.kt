package com.example.mobileappfinal

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappfinal.databinding.FragmentRestaurantDetailBinding
import com.example.mobileappfinal.model.Restaurant
import com.google.android.material.navigation.NavigationView

// RestaurantDetailFragment.kt
class RestaurantDetailFragment : Fragment(R.layout.fragment_restaurant_detail), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: FragmentRestaurantDetailBinding? = null
    private val binding get() = _binding!!

    val foodItems = listOf(
        Restaurant(
            name = "Chipotle",
            location = "Location 1",
            photos = listOf("chipotle_photo1.jpg", "chipotle_photo2.jpg", "chipotle_photo3.jpg"),
            foodItems = mapOf(
                "Chicken" to 8.50,
                "Steak" to 9.50,
                "Veggie" to 7.00,
                "Drink" to 2.00,
                "Chips" to 2.50
            )
        ),
        Restaurant(
            name = "Chick-Fil-A",
            location = "Location 2",
            photos = listOf("chickfila_photo1.jpg", "chickfila_photo2.jpg", "chickfila_photo3.jpg"),
            foodItems = mapOf(
                "Sandwich" to 5.50,
                "Fries" to 2.50,
                "Drink" to 2.00,
                "Nuggets" to 6.50,
                "Salad" to 8.50
            )
        ),
        Restaurant(
            name = "Burger King",
            location = "Location 3",
            photos = listOf("burgerking_photo1.jpg", "burgerking_photo2.jpg", "burgerking_photo3.jpg"),
            foodItems = mapOf(
                "Whopper" to 7.00,
                "Chicken Sandwich" to 6.50,
                "Fries" to 2.00,
                "Drink" to 1.50,
                "Cookie" to 1.00
            )
        ),
        Restaurant(
            name = "Panera",
            location = "Location 4",
            photos = listOf("panera_photo1.jpg", "panera_photo2.jpg", "panera_photo3.jpg"),
            foodItems = mapOf(
                "Chicken and Bacon Sandwich" to 11.00,
                "Chicken Salad" to 8.50,
                "Bagel" to 3.00,
                "Drink" to 2.50,
                "Apple" to 2.00
            )
        ),
        Restaurant(
            name = "McDonald's",
            location = "Location 5",
            photos = listOf("mcdonalds_photo1.jpg", "mcdonalds_photo2.jpg", "mcdonalds_photo3.jpg"),
            foodItems = mapOf(
                "Big Mac" to 8.50,
                "McNuggets" to 7.50,
                "McChicken" to 4.00,
                "Fries" to 3.00,
                "Drink" to 1.00
            )
        ),
        Restaurant(
            name = "Z and C",
            location = "Location 6",
            photos = listOf("zandc_photo1.jpg", "zandc_photo2.jpg", "zandc_photo3.jpg"),
            foodItems = mapOf(
                "Chicken Teriyaki" to 6.00,
                "Sushi" to 7.00,
                "Dumplings" to 5.00,
                "Drink" to 2.00,
                "Soup" to 2.00
            )
        ),
        Restaurant(
            name = "Taco Bell",
            location = "Location 7",
            photos = listOf("tacobell_photo1.jpg", "tacobell_photo2.jpg", "tacobell_photo3.jpg"),
            foodItems = mapOf(
                "Burrito" to 3.00,
                "Taco" to 2.00,
                "Party Pack" to 12.00,
                "Quesadilla" to 7.00,
                "Drink" to 2.00
            )
        ),
        Restaurant(
            name = "Chili's",
            location = "Location 8",
            photos = listOf("chilis_photo1.jpg", "chilis_photo2.jpg", "chilis_photo3.jpg"),
            foodItems = mapOf(
                "Burger" to 10.00,
                "Chicken Sandwich" to 8.00,
                "Wings" to 7.00,
                "Mac and Cheese" to 12.00,
                "Drink" to 2.50
            )
        )
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRestaurantDetailBinding.bind(view)

        // Set up toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = ""

        // Configure drawer toggle
        val drawerToggle = ActionBarDrawerToggle(
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

        // Retrieve the passed restaurant name from arguments
        val args = RestaurantDetailFragmentArgs.fromBundle(requireArguments())
        val restaurantName = args.restaurantName

        // Find the corresponding restaurant from the list
        val restaurant = foodItems.find { it.name == restaurantName }

        // Populate the UI with the restaurant data
        restaurant?.let {
            binding.restaurantName.text = it.name
            binding.location.text = it.location

            // Convert food items map to a list of strings (food name and price)
            val menuItems = it.foodItems.map { (food, price) -> "$food: $$price" }

            val initialCounts = mutableMapOf<String, Int>()
            it.foodItems.keys.forEach { foodName ->
                initialCounts[foodName] = 0 // Initializing all counts to 0
            }

            // Set up RecyclerView
            binding.foodItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.foodItemsRecyclerView.adapter = FoodItemAdapter(menuItems, initialCounts)
        }

        binding.orderButton.setOnClickListener {
            val orderedItems = mutableListOf<Pair<String, Int>>()

            for (i in 0 until binding.foodItemsRecyclerView.childCount) {
                val itemView = binding.foodItemsRecyclerView.getChildAt(i)
                val foodNameTextView = itemView.findViewById<TextView>(R.id.foodItem)
                val quantityTextView = itemView.findViewById<TextView>(R.id.itemCount)

                val foodName = foodNameTextView.text.toString()
                val quantity = quantityTextView.text.toString().toIntOrNull() ?: 0

                if (quantity > 0) {
                    orderedItems.add(foodName to quantity)
                }
            }

            val bundle = Bundle().apply {
                putSerializable("ordered_items", ArrayList(orderedItems))
            }
            findNavController().navigate(R.id.detailToCheckout, bundle)
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> findNavController().navigate(R.id.detailToHome)
            R.id.nav_recent_orders -> findNavController().navigate(R.id.detailToRecentOrders)
            R.id.nav_calendar -> findNavController().navigate(R.id.nav_calendar)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
