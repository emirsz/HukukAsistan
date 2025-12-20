package com.emirsoylemez.hukukasistan

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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


        // 1. supportFragmentManager aracılığıyla NavHostFragment'ı bulun.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // 2. NavController'ı doğrudan NavHostFragment'tan alın.
        val navController = navHostFragment.navController

        // 3. BottomNavigationView'ı bulunan navController ile kurun.
        binding.bottom.setupWithNavController(navController)


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