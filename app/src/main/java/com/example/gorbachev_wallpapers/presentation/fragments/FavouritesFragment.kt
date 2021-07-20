package com.example.gorbachev_wallpapers.presentation.fragments

import android.os.Bundle
import android.view.View
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentFavouritesBinding
import com.example.gorbachev_wallpapers.presentation.adapters.FavouritesFragmentsAdapter
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment


class FavouritesFragment : BaseFragment(R.layout.fragment_favourites) {
	
	private var _binding: FragmentFavouritesBinding? = null
	private val binding get() = _binding!!
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		_binding = FragmentFavouritesBinding.bind(view)
		
		setUpTabs()
	}
	
	private fun setUpTabs() {
		binding.apply {
			val adapter = FavouritesFragmentsAdapter(childFragmentManager)
			adapter.addFragment(FavouritesImagesFragment(), title = "images")
			adapter.addFragment(FavouritesQueryFragment(), title = "queries")
			viewPagerFavourites.adapter = adapter
			tabLayoutFavourites.setupWithViewPager(viewPagerFavourites)
			tabLayoutFavourites.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_image_24)
			tabLayoutFavourites.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_favorite_24)
			
		}
		
	}
}