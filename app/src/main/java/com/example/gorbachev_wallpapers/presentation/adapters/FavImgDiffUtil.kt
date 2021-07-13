package com.example.gorbachev_wallpapers.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.gorbachev_wallpapers.models.Images

class FavImgDiffUtil(
	private val oldList: List<Images>,
	private val newList: List<Images>
) : DiffUtil.Callback() {
	override fun getOldListSize(): Int {
		return oldList.size
	}
	
	override fun getNewListSize(): Int {
		return newList.size
	}
	
	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].id == newList[newItemPosition].id
	}
	
	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return when {
			oldList[oldItemPosition].id != newList[newItemPosition].id -> {
				false
			}
			oldList[oldItemPosition].img != newList[newItemPosition].img -> {
				false
			}
			else -> true
		}
	}
}