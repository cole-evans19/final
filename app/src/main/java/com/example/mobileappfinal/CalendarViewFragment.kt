package com.example.mobileappfinal

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import com.example.mobileappfinal.databinding.CalendarViewBinding
import com.google.android.material.navigation.NavigationView

class CalendarFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: CalendarViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = CalendarViewBinding.inflate(inflater, container, false)

        // Set up the toolbar (if it's part of the layout)
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = "Calendar"

        // Set up the drawer toggle and listener
        val drawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Initialize the CalendarView
        val calendarView: CalendarView = binding.calendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(activity, "Selected date: $selectedDate", Toast.LENGTH_SHORT).show()
        }

        // Set the navigation item listener
        binding.navigationView.setNavigationItemSelectedListener(this)

        return binding.root
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_recent_orders -> findNavController().navigate(R.id.calendarToOrders)
            R.id.nav_home -> findNavController().navigate(R.id.checkoutToHome)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
