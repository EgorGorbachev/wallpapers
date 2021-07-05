package com.example.gorbachev_wallpapers.presentation.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FragmentGalleryBinding
import com.example.gorbachev_wallpapers.presentation.adapters.UnsplashRecyclerAdapter
import com.example.gorbachev_wallpapers.viewmodels.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {
	
	private val viewModel by viewModels<GalleryViewModel>()
	
	private var _binding: FragmentGalleryBinding? = null
	private val binding get() = _binding!!
	private lateinit var img: CardView
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		_binding = FragmentGalleryBinding.bind(view)
		val adapter = UnsplashRecyclerAdapter()
		
		
		
		binding.apply {
			imageRecyclerView.setHasFixedSize(true)
			
			imageRecyclerView.adapter = adapter
		}
		
		viewModel.photos.observe(viewLifecycleOwner) {
			adapter.submitData(viewLifecycleOwner.lifecycle, it)
		}
	}
	
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}