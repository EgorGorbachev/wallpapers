package com.example.gorbachev_wallpapers.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gorbachev_wallpapers.R
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.viewmodels.FavouritesImagesViewModel
import com.example.gorbachev_wallpapers.viewmodels.ImagesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FavouritesImagesRecyclerAdapter(
	private var imgList: List<Images>,
	private var viewModel: FavouritesImagesViewModel
) : RecyclerView.Adapter<FavouritesImagesRecyclerAdapter.MyViewHolder>() {
	
	
	class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var img: ImageView = itemView.findViewById(R.id.favouritesImageIV)
		var deleteBtn: FloatingActionButton = itemView.findViewById(R.id.deleteFavImgButton)
		
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
		val itemView =
			LayoutInflater.from(parent.context)
				.inflate(R.layout.favourites_images_recycler_item, parent, false)
		return MyViewHolder(itemView)
	}
	
	
	override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
		holder.img.setImageBitmap(imgList[position].img)
		holder.deleteBtn.setOnClickListener {
			viewModel.deleteFromDatabase(imgList[position])
		}
	}
	
	override fun getItemCount() = imgList.size
	
	fun setData(newPersonsList: List<Images>) {
		val diffUtil = FavImgDiffUtil(imgList, newPersonsList)
		val diffResults = DiffUtil.calculateDiff(diffUtil)
		imgList = newPersonsList
		val s: Set<Images> = LinkedHashSet(imgList)
		imgList = s.toList()
		diffResults.dispatchUpdatesTo(this)
	}
	
}