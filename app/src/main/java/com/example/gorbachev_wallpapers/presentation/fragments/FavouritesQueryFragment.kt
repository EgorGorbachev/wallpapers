package com.example.gorbachev_wallpapers.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_gmail.sharedPref.SharedPreferences
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentFavouritesImagesBinding
import com.example.gorbachev_wallpapers.databinding.FragmentFavouritesQueryBinding
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.presentation.adapters.FavouritesQueriesAdapter
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.example.gorbachev_wallpapers.sharedPref.QUERY
import com.example.gorbachev_wallpapers.viewmodels.FavouritesQueriesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouritesQueryFragment : BaseFragment(R.layout.fragment_favourites_query),
	FavouritesQueriesAdapter.OnItemClick, FavouritesQueriesAdapter.OnDeleteClick {
	
	private val viewModel by viewModels<FavouritesQueriesViewModel>()
	
	private var adapter = FavouritesQueriesAdapter(this, this)
	
	private var _binding: FragmentFavouritesQueryBinding? = null
	private val binding get() = _binding!!
	
	private lateinit var SP: SharedPreferences
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		val recyclerView: RecyclerView = requireView().findViewById(R.id.favouriteQueriesContainer)
		recyclerView.setHasFixedSize(true)
		recyclerView.adapter = adapter
		
		SP = SharedPreferences(requireContext())
		
		_binding = FragmentFavouritesQueryBinding.bind(view)
		showQueries()
	}
	
	private fun showQueries() {
		viewModel.allData.observe(viewLifecycleOwner, {
			adapter.submitList(it)
		})
	}
	
	override fun onItemClick(query: Queries) {
		SP.setPref(QUERY, query.query)
		val action = FavouritesFragmentDirections.actionFavouritesFrToSearchFr()
		findNavController().navigate(action)
	}
	
	override fun onDeleteClick(query: Queries) {
		TODO("Not yet implemented")
	}
	
}