package com.example.gorbachev_wallpapers.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.FavouritesImagesRecyclerItemBinding
import com.example.gorbachev_wallpapers.models.Images

class FavouritesImagesRecyclerAdapter(
	private var listener: OnItemClick,
	private var deleteListener: DeleteItem,
) : ListAdapter<Images, FavouritesImagesRecyclerAdapter.MyViewHolder>(PHOTO_COMPARATOR) {
	
	
	inner class MyViewHolder(private var binding: FavouritesImagesRecyclerItemBinding) :
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
			binding.deleteFavImgButton.setOnClickListener {
				val position = bindingAdapterPosition
				if (position != RecyclerView.NO_POSITION) {
					val item = getItem(position)
					if (item != null) {
						deleteListener.deleteItem(item)
					}
				}
			}
		}
		
		fun bind(image: Images) {
			binding.apply {
				Glide.with(itemView).load(image.img).centerCrop()
					.transition(DrawableTransitionOptions.withCrossFade())
					.error(R.drawable.ic_baseline_error_24).into(favouritesImageIV)
				
			}
			
			
		}
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
		val binding =
			FavouritesImagesRecyclerItemBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		return MyViewHolder(binding)
	}
	
	
	override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
		val currentItem = getItem(position)
		
		if (currentItem != null) {
			holder.bind(currentItem)
		}
	}
	
	interface OnItemClick {
		fun onItemClick(image: Images)
	}
	
	interface DeleteItem {
		fun deleteItem(image: Images)
	}
	
	companion object {
		private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Images>() {
			override fun areItemsTheSame(oldItem: Images, newItem: Images) =
				oldItem.id == newItem.id
			
			override fun areContentsTheSame(
				oldItem: Images,
				newItem: Images
			) = oldItem == newItem
		}
		
	}
	
}
