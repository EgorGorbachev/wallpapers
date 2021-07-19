package com.example.gorbachev_wallpapers.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_wallpapers.databinding.HistoryItemBinding
import com.example.gorbachev_wallpapers.models.Queries

class QueriesHistoryRecyclerAdapter(
	private var listener: OnItemClick,
	private var listenerLike: OnLikeClick,
) : ListAdapter<Queries, QueriesHistoryRecyclerAdapter.MyViewHolder>(PHOTO_COMPARATOR) {
	
	inner class MyViewHolder(private var binding: HistoryItemBinding) :
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
		
		@SuppressLint("SetTextI18n")
		fun bind(query: Queries) {
			binding.apply {
				queryHistoryTitle.text = query.query
				queryHistoryInfo.text = "${query.queryCount}" + ", " + query.time
			}
			
		}
	}
	
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): QueriesHistoryRecyclerAdapter.MyViewHolder {
		val binding =
			HistoryItemBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		return MyViewHolder(binding)
	}
	
	override fun onBindViewHolder(
		holder: QueriesHistoryRecyclerAdapter.MyViewHolder,
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
	
	interface OnLikeClick {
		fun onLikeClick(query: Queries)
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