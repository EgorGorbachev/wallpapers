package com.example.gorbachev_wallpapers.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentGalleryBinding
import com.example.gorbachev_wallpapers.models.UnsplashPhoto
import com.example.gorbachev_wallpapers.presentation.adapters.UnsplashLoadStateAdapter
import com.example.gorbachev_wallpapers.presentation.adapters.UnsplashRecyclerAdapter
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.example.gorbachev_wallpapers.viewmodels.GalleryViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class GalleryFragment : BaseFragment(R.layout.fragment_gallery), UnsplashRecyclerAdapter.OnItemClickListener {
	
	private val viewModel by viewModels<GalleryViewModel>()
	
	private var _binding: FragmentGalleryBinding? = null
	private val binding get() = _binding!!
	
	private lateinit var searchView: SearchView
	
	private lateinit var appbar: AppBarLayout
	
	private lateinit var fabColumn: FloatingActionButton
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		_binding = FragmentGalleryBinding.bind(view)
		val adapter = UnsplashRecyclerAdapter(this)
		
		appbar = binding.imageAppBar
		appbar.setBackgroundColor(Color.TRANSPARENT)
		
		
		val footerAdapter = UnsplashLoadStateAdapter { adapter.retry() }
		val recyclerView = binding.imageRecyclerView
		val layoutManager = GridLayoutManager(requireContext(), 2)
		recyclerView.layoutManager = layoutManager
		recyclerView.setHasFixedSize(true)
		recyclerView.adapter = adapter.withLoadStateFooter(
			footer = footerAdapter
		)
		layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
			override fun getSpanSize(position: Int): Int {
				return if (position == adapter.itemCount && footerAdapter.itemCount > 0) {
					2
				} else {
					1
				}
			}
		}
		
		
		viewModel.photos.observe(viewLifecycleOwner) {
			adapter.submitData(viewLifecycleOwner.lifecycle, it)
		}
		
		searchView = binding.imageSearchView
		searchView.isIconifiedByDefault = false;
		searchView.clearFocus()
		searchView.queryHint = "forest"
		
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				if (query != null) {
					binding.imageRecyclerView.scrollToPosition(0)
					viewModel.searchPhotos(query)
					searchView.clearFocus()
				}
				return true
			}
			
			override fun onQueryTextChange(newText: String?): Boolean {
				return true
			}
		})
		
		if(!isOnline()){
			toast("You have no internet connection!")
			binding.imageRecyclerView.isVisible = false
			binding.isOnlineTV.isVisible = true
			binding.imageAppBar.isVisible = false
		}
		var columnCount = 2
		fabColumn = binding.fabColumn
		fabColumn.setOnClickListener {
			when(columnCount){
				2 -> {
					(binding.imageRecyclerView.layoutManager as GridLayoutManager).spanCount = 3
					fabColumn.setImageResource(R.drawable.ic_2_columns)
					columnCount = 3
				}
				3 -> {
					(binding.imageRecyclerView.layoutManager as GridLayoutManager).spanCount = 2
					fabColumn.setImageResource(R.drawable.ic_3_columns)
					columnCount = 2
				}
			}
			
		}
		
	}
	
	override fun onItemClick(photo: UnsplashPhoto) {
		val action = GalleryFragmentDirections.actionSearchFrToDetailsImageFragment(photo, viewModel.getCurrentQuery())
		findNavController().navigate(action)
	}
	
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	private fun isOnline(): Boolean {
		val runtime = Runtime.getRuntime()
		try {
			val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
			val exitValue = ipProcess.waitFor()
			return exitValue == 0
		} catch (e: IOException) {
			e.printStackTrace()
		} catch (e: InterruptedException) {
			e.printStackTrace()
		}
		return false
	}
}