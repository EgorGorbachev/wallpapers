package com.example.gorbachev_wallpapers.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentFavouritesImagesBinding
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.presentation.adapters.FavouritesImagesRecyclerAdapter
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.example.gorbachev_wallpapers.presentation.util.Converters
import com.example.gorbachev_wallpapers.viewmodels.FavouritesImagesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavouritesImagesFragment : BaseFragment(R.layout.fragment_favourites_images) {
	
	private val viewModel by viewModels<FavouritesImagesViewModel>()
	
	private lateinit var adapter: FavouritesImagesRecyclerAdapter
	
	private lateinit var list: List<Images>
	
	private var _binding: FragmentFavouritesImagesBinding? = null
	private val binding get() = _binding!!
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		_binding = FragmentFavouritesImagesBinding.bind(view)
		showImage()
		GlobalScope.launch(Dispatchers.Main) {
			delay(1000)
			adapter.setData(list)
		}
	}
	
	private fun showImage() {
		viewModel.allData.observe(viewLifecycleOwner,{
			adapter = FavouritesImagesRecyclerAdapter(it, viewModel)
			val recyclerView: RecyclerView = requireView().findViewById(R.id.favouriteImagesContainer)
			recyclerView.layoutManager = LinearLayoutManager(requireContext())
			recyclerView.adapter = adapter
			list = it
		})

	}
}