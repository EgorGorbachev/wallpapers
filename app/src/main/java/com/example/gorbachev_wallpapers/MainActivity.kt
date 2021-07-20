package com.example.gorbachev_wallpapers

import android.database.CursorWindow
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	
	private lateinit var botNav: BottomNavigationViewEx
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		botNav = findViewById(R.id.bot_nav)
		botNav.enableAnimation(true)
		botNav.enableShiftingMode(true)
		
		val navHostFragment =
			supportFragmentManager.findFragmentById((R.id.fragmentContainerView)) as NavHostFragment
		val navController = navHostFragment.findNavController()
		botNav.setupWithNavController(navController)
		
		try {
			val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
			field.isAccessible = true
			field.set(null, 100 * 1024 * 1024)
		} catch (e: Exception) {
			e.printStackTrace()
		}
		
	}
}