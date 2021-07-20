package com.example.gorbachev_wallpapers.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentFavouritesImagesBinding
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.presentation.adapters.FavouritesImagesRecyclerAdapter
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.example.gorbachev_wallpapers.viewmodels.FavouritesImagesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouritesImagesFragment : BaseFragment(R.layout.fragment_favourites_images),
	FavouritesImagesRecyclerAdapter.OnItemClick, FavouritesImagesRecyclerAdapter.DeleteItem {
	
	private val viewModel by viewModels<FavouritesImagesViewModel>()
	
	private var adapter = FavouritesImagesRecyclerAdapter(this, this)
	
	private lateinit var list: List<Images>
	
	private var _binding: FragmentFavouritesImagesBinding? = null
	private val binding get() = _binding!!
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		val recyclerView: RecyclerView = requireView().findViewById(R.id.favouriteImagesContainer)
		recyclerView.setHasFixedSize(true)
		recyclerView.adapter = adapter
		val layoutManager = LinearLayoutManager(requireContext())
		layoutManager.reverseLayout = true
		layoutManager.stackFromEnd = true
		recyclerView.layoutManager = layoutManager
		
		_binding = FragmentFavouritesImagesBinding.bind(view)
		showImage()
	}
	
	private fun showImage() {
		viewModel.allData.observe(viewLifecycleOwner, {
			adapter.submitList(it)
			binding.nullImagesMes.isVisible = it.isNullOrEmpty()
		})
	}
	
	override fun onItemClick(image: Images) {
		val action = FavouritesFragmentDirections.actionFavouritesFrToDetailsImageFragment(
			image = image
		)
		findNavController().navigate(action)
	}
	
	override fun deleteItem(image: Images) {
		viewModel.deleteFromDatabase(image)
	}
	
}