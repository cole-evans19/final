package com.example.mobileappfinal

import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappfinal.databinding.FragmentRecentOrdersBinding
import com.example.mobileappfinal.model.Order
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class RecentOrdersFragment : Fragment(R.layout.fragment_recent_orders), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: FragmentRecentOrdersBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val ordersAdapter = OrdersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecentOrdersBinding.bind(view)

        // Set up toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = "Recent Orders"

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

        // Set navigation listener
        binding.navView.setNavigationItemSelectedListener(this)

        // Set up RecyclerView
        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOrders.adapter = ordersAdapter

        // Fetch orders from Firestore
        fetchOrdersFromFirestore()
    }

    private fun fetchOrdersFromFirestore() {
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                val ordersList = result.documents.mapNotNull { doc ->
                    val Restaurant = doc.getString("restaurantName")
                    val Food = doc.get("orderedItems") as? List<Map<String, Any>>
                    val Address = doc.getString("address")
                    val Date = doc.getString("orderTime")
                    val Price = doc.getDouble("totalPrice")
                    val Quantity = Food?.sumOf { it["quantity"] as? Long ?: 0 } ?: 0L

                    if (Restaurant != null && Address != null && Date != null && Price != null) {
                        Order(
                            Restaurant,
                            Food?.joinToString(", ") { "${it["name"]}" } ?: "N/A",
                            Address,
                            Date,
                            Price,
                            Quantity.toInt()
                        )
                    } else {
                        null
                    }
                }

                ordersAdapter.submitList(ordersList)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> findNavController().navigate(R.id.recentsToHome)
            R.id.nav_calendar -> findNavController().navigate(R.id.recentsToCalendar)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
