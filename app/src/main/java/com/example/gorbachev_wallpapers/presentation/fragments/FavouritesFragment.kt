package com.example.gorbachev_wallpapers.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment


class FavouritesFragment : BaseFragment(R.layout.fragment_favourites) {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_favourites, container, false)
	}

}