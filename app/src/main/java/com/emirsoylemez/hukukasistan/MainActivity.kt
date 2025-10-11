package com.emirsoylemez.hukukasistan

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emirsoylemez.hukukasistan.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        binding.bottom.setOnItemSelectedListener { item ->
//            val navController = findNavController(R.id.fragmentContainerView)
//            when (item.itemId) {
//                R.id.home -> navController.navigate(R.id.HomeFragment)
//                //R.id.addExpense -> replaceFragment()
//                R.id.dava -> navController.navigate(R.id.expenseFragment)
//                R.id.consult -> navController.navigate(R.id.profileFragment)
//            }
//            true
//        }


        //        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom)
        bottomNavigationView.visibility = visibility
    }

    fun setToolbarVisibility(visibility: Int) {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.visibility = visibility
    }

}