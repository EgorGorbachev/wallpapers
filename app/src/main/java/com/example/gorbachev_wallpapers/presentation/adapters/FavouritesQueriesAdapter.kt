package com.example.gorbachev_wallpapers.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_wallpapers.databinding.FavouritesQueriesRecyclerItemBinding
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.presentation.adapters.FavouritesQueriesAdapter.*

class FavouritesQueriesAdapter(
	private var listener: OnItemClick,
	private var listenerDelete: OnDeleteClick
) : ListAdapter<Queries, MyViewHolder>(PHOTO_COMPARATOR) {
	
	inner class MyViewHolder(private var binding: FavouritesQueriesRecyclerItemBinding) :
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
			
			binding.favQueryDeleteBtn.setOnClickListener {
				val position = bindingAdapterPosition
				if (position != RecyclerView.NO_POSITION) {
					val item = getItem(position)
					if (item != null) {
						listenerDelete.onDeleteClick(item)
					}
				}
			}
		}
		
		@SuppressLint("SetTextI18n")
		fun bind(query: Queries) {
			binding.apply {
				favQueryTitle.text = query.query
				favQueryInfo.text = "${query.queryCount}" + ", " + query.time
			}
			
		}
	}
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): MyViewHolder {
		val binding =
			FavouritesQueriesRecyclerItemBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		return MyViewHolder(binding)
	}
	
	override fun onBindViewHolder(
		holder: MyViewHolder,
		position: Int
	) {
		val currentItem = getItem(position)
		
		if (currentItem != null) {
			holder.bind(currentItem)
		}
	}
	
	interface OnItemClick {
		fun onItemClick(query: Queries)
	}
	
	interface OnDeleteClick {
		fun onDeleteClick(query: Queries)
	}
	
	companion object {
		private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Queries>() {
			override fun areItemsTheSame(
				oldItem: Queries,
				newItem: Queries
			) = oldItem.id == newItem.id
			
			override fun areContentsTheSame(
				oldItem: Queries,
				newItem: Queries
			) = oldItem == newItem
		}
		
	}
	
}