package com.example.gorbachev_wallpapers.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.RecyclerItemBinding
import com.example.gorbachev_wallpapers.models.UnsplashPhoto


class UnsplashRecyclerAdapter(private val listener: OnItemClickListener) :
	PagingDataAdapter<UnsplashPhoto, UnsplashRecyclerAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	)
			: PhotoViewHolder {
		val binding =
			RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return PhotoViewHolder(binding)
	}
	
	override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
		val currentItem = getItem(position)
		
		if (currentItem != null) {
			holder.bind(currentItem)
			
		}
	}
	
	inner class PhotoViewHolder(private val binding: RecyclerItemBinding) :
		RecyclerView.ViewHolder(binding.root) {
		
		init {
			binding.root.setOnClickListener {
				val position = bindingAdapterPosition
				if (position != RecyclerView.NO_POSITION) {
					val item = getItem(position)
					if (item != null) {
						listener.onItemClick(item)
					}
				}
				
			}
		}
		
		fun bind(photo: UnsplashPhoto) {
			binding.apply {
				Glide.with(itemView).load(photo.urls.regular).centerCrop()
					.transition(DrawableTransitionOptions.withCrossFade())
					.error(R.drawable.ic_baseline_error_24).into(imageViewInCard)
			}
			
		}
		
	}
	
	interface OnItemClickListener {
		fun onItemClick(photo: UnsplashPhoto)
	}
	
	companion object {
		private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
			override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
				oldItem.id == newItem.id
			
			override fun areContentsTheSame(
				oldItem: UnsplashPhoto,
				newItem: UnsplashPhoto
			) = oldItem == newItem
		}
		
	}
	
	
}