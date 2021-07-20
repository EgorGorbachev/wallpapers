package com.example.gorbachev_wallpapers.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_wallpapers.databinding.RecyclerLoadBinding

class UnsplashLoadStateAdapter(private val retry: () -> Unit) :
	LoadStateAdapter<UnsplashLoadStateAdapter.LoadStateViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
		val binding =
			RecyclerLoadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		
		return LoadStateViewHolder(binding)
	}
	
	override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
		holder.bind(loadState)
	}
	
	
	inner class LoadStateViewHolder(private val binding: RecyclerLoadBinding) :
		RecyclerView.ViewHolder(binding.root) {
		
		init {
			binding.imageLoadingRetryBtn.setOnClickListener {
				retry.invoke()
			}
		}
		
		fun bind(loadState: LoadState) {
			binding.apply {
				imageLoadingProgressbar.isVisible = loadState is LoadState.Loading
				imageLoadingRetryBtn.isVisible = loadState !is LoadState.Loading
				imageLoadingTV.isVisible = loadState !is LoadState.Loading
			}
		}
	}
}