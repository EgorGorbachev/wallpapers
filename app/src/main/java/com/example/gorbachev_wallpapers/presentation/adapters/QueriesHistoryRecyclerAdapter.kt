package com.example.gorbachev_wallpapers.presentation.adapters

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.databinding.HistoryItemBinding
import com.example.gorbachev_wallpapers.models.Queries
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes

class QueriesHistoryRecyclerAdapter(
	private var listener: OnItemClick,
	private var listenerLike: OnLikeClick
) : ListAdapter<Queries, QueriesHistoryRecyclerAdapter.MyViewHolder>(PHOTO_COMPARATOR) {
	
	lateinit var res:Resources
	
	inner class MyViewHolder(private var binding: HistoryItemBinding) :
		RecyclerView.ViewHolder(binding.root) {
		
		init {
			res = binding.root.resources
			binding.root.setOnClickListener {
				val position = bindingAdapterPosition
				if (position != RecyclerView.NO_POSITION) {
					val item = getItem(position)
					if (item != null) {
						listener.onItemClick(item)
					}
				}
			}
			
			binding.queryHistoryLikeButton.setOnClickListener {
				val position = bindingAdapterPosition
				if (position != RecyclerView.NO_POSITION) {
					val item = getItem(position)
					if (item != null) {
						listenerLike.onLikeClick(item)
					}
				}
			}
		}
		
		@SuppressLint("SetTextI18n", "SimpleDateFormat")
		fun bind(query: Queries) {
			binding.apply {
				queryHistoryTitle.text = query.query
				
				queryHistoryInfo.text = "${query.queryCount}" + ", " + time(query.time)
				if (!query.like) {
					queryHistoryLikeButton.setImageResource(R.drawable.ic_baseline_favorite_24)
				} else queryHistoryLikeButton.setImageResource(R.drawable.ic_baseline_favorite_true_24)
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
			) = oldItem.query == newItem.query
			
			override fun areContentsTheSame(
				oldItem: Queries,
				newItem: Queries
			) = oldItem == newItem
		}
		
	}
	
	private fun time(time: String): String {
		val queryTime = DateTime.parse(time)
		val curTime = DateTime.now()
		
		val minutesBetween = Minutes.minutesBetween(queryTime, curTime)
		val hoursBetween = Hours.hoursBetween(queryTime, curTime)
		val daysBetween = Days.daysBetween(queryTime, curTime)
		
		var mTime = ""
		
		if (minutesBetween.minutes <= 5) {
			mTime = res.getString(R.string.query_time_less_five_min)
		} else {
			if (minutesBetween.minutes > 5 && hoursBetween.hours == 0) {
				mTime = res.getString(R.string.query_time_less_hour)
			} else {
				if (hoursBetween.hours != 0 && daysBetween.days == 0) {
					mTime = "${hoursBetween.hours}" + res.getString(R.string.query_time_count_hours)
				} else {
					if (daysBetween.days != 0) {
						mTime = "${queryTime.dayOfMonth()} ${queryTime.monthOfYear()}"
					}
				}
			}
		}
		return mTime
	}
}