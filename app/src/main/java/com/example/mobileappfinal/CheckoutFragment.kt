package com.example.mobileappfinal

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileappfinal.databinding.FragmentCheckoutBinding
import com.google.android.material.navigation.NavigationView

class CheckoutFragment : Fragment(R.layout.fragment_checkout), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val orderedItems = mutableListOf<Pair<String, Int>>()


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

        val receivedItems = arguments?.getSerializable("ordered_items") as? ArrayList<Pair<String, Int>>
        if (receivedItems != null) {
            orderedItems.addAll(receivedItems)
        }

        // Set up RecyclerView
        binding.orderedItemsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.orderedItemsRecyclerView.adapter = CheckoutAdapter(orderedItems)
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
