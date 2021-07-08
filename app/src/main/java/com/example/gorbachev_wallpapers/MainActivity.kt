package com.example.gorbachev_wallpapers

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gorbachev_wallpapers.models.UnsplashPhoto
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	
	private lateinit var botNav: BottomNavigationViewEx

	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		botNav = findViewById(R.id.bot_nav)
		botNav.enableAnimation(true)
		botNav.enableShiftingMode(true)
		
		val navHostFragment = supportFragmentManager.findFragmentById((R.id.fragmentContainerView)) as NavHostFragment
		val navController = navHostFragment.findNavController()
		botNav.setupWithNavController(navController)
		
	}
}