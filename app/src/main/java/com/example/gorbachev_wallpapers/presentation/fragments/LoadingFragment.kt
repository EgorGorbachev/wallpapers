package com.example.gorbachev_wallpapers.presentation.fragments

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoadingFragment : BaseFragment(R.layout.fragment_loading) {
	
	private lateinit var botNav: BottomNavigationView
	
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_loading, container, false)
		
		botNav = requireActivity().findViewById(R.id.bot_nav)
		botNav.visibility = View.GONE
		
		
		GlobalScope.launch(Dispatchers.Main) {
			delay(2000)
			Navigation.findNavController(requireView())
				.navigate(R.id.action_loadingFragment_to_search_fr)
			botNav.visibility = View.VISIBLE
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			val window: Window = requireActivity().window
			val startColor: Int = window.statusBarColor
			val endColor = ContextCompat.getColor(requireContext(), R.color.white)
			ObjectAnimator.ofArgb(
				window,
				"statusBarColor",
				startColor,
				endColor
			).start()
		}
		
		return view
	}
	
}