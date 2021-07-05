package com.example.gorbachev_wallpapers.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment

class HistoryFragment : BaseFragment(R.layout.fragment_history) {
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		val view = inflater.inflate(R.layout.fragment_history, container, false)
		
		return view
	}
	
}