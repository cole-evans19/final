package com.example.mobileappfinal

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappfinal.databinding.FragmentCheckoutBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckoutFragment : Fragment(R.layout.fragment_checkout), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val orderedItems = mutableListOf<Pair<String, Int>>() // Stores ordered food items
    private val db = FirebaseFirestore.getInstance() // Firebase Firestore instance

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCheckoutBinding.bind(view)

        // Set up toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = "Checkout"

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

        // Retrieve ordered items from arguments
        val receivedItems = arguments?.getSerializable("ordered_items") as? ArrayList<Pair<String, Int>>
        if (receivedItems != null) {
            orderedItems.addAll(receivedItems)
        }

        // Set up RecyclerView to display ordered items
        binding.orderedItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderedItemsRecyclerView.adapter = CheckoutAdapter(orderedItems)

        // Handle Place Order button
        binding.placeOrderButton.setOnClickListener {
            val address = binding.addressEditText.text.toString().trim()
            val specialInstructions = binding.specialInstructionsEditText.text.toString().trim()

            if (address.isEmpty()) {
                binding.addressEditText.error = "Please enter your delivery address"
                return@setOnClickListener
            }

            val restaurantName = "Your Restaurant Name" // Replace with actual restaurant name
            val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            // Prepare order data for Firestore
            val orderData = hashMapOf(
                "restaurantName" to restaurantName,
                "address" to address,
                "orderTime" to currentDateTime,
                "orderedItems" to orderedItems.map { mapOf("name" to it.first, "quantity" to it.second) },
            )

            // Save to Firestore
            db.collection("orders")
                .add(orderData)
                .addOnSuccessListener {
                    Log.d("Firestore", "Order successfully saved!")
                    Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show()

                    // Navigate to OrderDetailsFragment
                    val bundle = Bundle().apply {
                        putSerializable("ordered_items", ArrayList(orderedItems))
                        putString("restaurant_name", restaurantName)
                        putString("address", address)
                        putString("special_instructions", specialInstructions)
                        putString("order_time", currentDateTime)
                    }
                    findNavController().navigate(R.id.checkoutToOrder, bundle)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error saving order", e)
                    Toast.makeText(requireContext(), "Failed to place order. Try again.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> findNavController().navigate(R.id.checkoutToHome)
            R.id.nav_recent_orders -> findNavController().navigate(R.id.checkoutToRecentOrders)
            R.id.nav_calendar -> findNavController().navigate(R.id.checkoutToCalendar)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
