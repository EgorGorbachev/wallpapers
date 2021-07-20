package com.example.gorbachev_wallpapers.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gorbachev_gmail.sharedPref.SharedPreferences
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentGalleryBinding
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.models.UnsplashPhoto
import com.example.gorbachev_wallpapers.presentation.adapters.UnsplashLoadStateAdapter
import com.example.gorbachev_wallpapers.presentation.adapters.UnsplashRecyclerAdapter
import com.example.gorbachev_wallpapers.presentation.base.BaseFragment
import com.example.gorbachev_wallpapers.sharedPref.QUERY
import com.example.gorbachev_wallpapers.viewmodels.GalleryViewModel
import com.example.gorbachev_wallpapers.viewmodels.QueriesViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.joda.time.DateTime
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class GalleryFragment : BaseFragment(R.layout.fragment_gallery),
	UnsplashRecyclerAdapter.OnItemClickListener {
	
	private val viewModel by viewModels<GalleryViewModel>()
	private val queriesViewModel by viewModels<QueriesViewModel>()
	
	private var _binding: FragmentGalleryBinding? = null
	private val binding get() = _binding!!
	
	private lateinit var botNav: BottomNavigationView
	
	private lateinit var searchView: SearchView
	
	private lateinit var appbar: AppBarLayout
	
	private lateinit var fabColumn: FloatingActionButton
	
	private lateinit var SP: SharedPreferences
	
	private var timer: Timer? = null
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		_binding = FragmentGalleryBinding.bind(view)
		val adapter = UnsplashRecyclerAdapter(this)
		
		botNav = requireActivity().findViewById(R.id.bot_nav)
		botNav.isVisible = true
		
		appbar = binding.imageAppBar
		appbar.setBackgroundColor(Color.TRANSPARENT)
		
		SP = SharedPreferences(requireContext())
		binding.imageRecyclerView.scrollToPosition(0)
		viewModel.searchPhotos(SP.getPrefString(QUERY)!!)
		
		fabColumn = binding.fabColumn
		
		val footerAdapter = UnsplashLoadStateAdapter { adapter.retry() }
		val recyclerView = binding.imageRecyclerView
		val layoutManager = GridLayoutManager(requireContext(), SP.getPrefInt("COUNT_COLUMN")!!)
		if (SP.getPrefInt("COUNT_COLUMN") == 2) {
			fabColumn.setImageResource(R.drawable.ic_3_columns)
		} else fabColumn.setImageResource(R.drawable.ic_2_columns)
		
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
		searchView.queryHint = SP.getPrefString(QUERY)
		
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				timer?.cancel()
				if (query != null) {
					binding.imageRecyclerView.scrollToPosition(0)
					viewModel.searchPhotos(query)
					searchView.clearFocus()
					SP.setPref(QUERY, query)
					insertQueryInDatabase(query, false, currentTime())
				}
				return true
			}
			
			
			override fun onQueryTextChange(newText: String?): Boolean {
				if (timer != null) {
					timer?.cancel()
				}
				if (!newText.isNullOrEmpty()) {
					timer = Timer()
					timer?.schedule(object : TimerTask() {
						override fun run() {
							Handler(Looper.getMainLooper()).postDelayed({
								binding.imageRecyclerView.scrollToPosition(0)
								viewModel.searchPhotos(newText)
								searchView.clearFocus()
								SP.setPref(QUERY, newText)
								insertQueryInDatabase(newText, false, currentTime())
							}, 0)
						}
					}, 2000)
				}
				return true
			}
		})
		
		
		if (!isOnline()) {
			toast("You have no internet connection!")
			binding.imageRecyclerView.isVisible = false
			binding.isOnlineTV.isVisible = true
			binding.imageAppBar.isVisible = false
		}
		
		
		fabColumn.setOnClickListener {
			when (SP.getPrefInt("COUNT_COLUMN")) {
				2 -> {
					(binding.imageRecyclerView.layoutManager as GridLayoutManager).spanCount = 3
					fabColumn.setImageResource(R.drawable.ic_2_columns)
					SP.setPref("COUNT_COLUMN", 3)
				}
				3 -> {
					(binding.imageRecyclerView.layoutManager as GridLayoutManager).spanCount = 2
					fabColumn.setImageResource(R.drawable.ic_3_columns)
					SP.setPref("COUNT_COLUMN", 2)
				}
			}
			
		}
		
	}
	
	
	private fun currentTime(): String {
		return DateTime.now().toString()
	}
	
	@DelicateCoroutinesApi
	private fun insertQueryInDatabase(query: String, like: Boolean, time: String) {
		GlobalScope.launch(Dispatchers.Main) {
			queriesViewModel.insertDatabase(
				
				Queries(query, like, viewModel.getTotal(query), time)
			)
		}
	}
	
	override fun onItemClick(photo: UnsplashPhoto) {
		val action = GalleryFragmentDirections.actionSearchFrToDetailsImageFragment(
			photo = photo,
			query = viewModel.getCurrentQuery()
		)
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