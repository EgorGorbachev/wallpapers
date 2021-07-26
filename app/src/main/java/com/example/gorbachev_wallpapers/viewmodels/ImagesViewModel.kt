package com.example.gorbachev_wallpapers.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.models.UnsplashPhoto
import com.example.gorbachev_wallpapers.repositories.UnsplashRepository
import kotlinx.android.synthetic.main.fragment_details_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ImagesViewModel @ViewModelInject constructor(
	private val repository: UnsplashRepository
) : ViewModel() {
	
	val allData: LiveData<List<Images>> = repository.allDataImages
	
	fun insertDatabase(image: Images) {
		GlobalScope.launch(Dispatchers.Default) {
			if (!isPhotoExists(image.img)) {
				repository.add(image)
			} else Log.d("duplicate", "duplicate")
		}
	}
	
	fun isPhotoExists(img: Bitmap): Boolean {
		return repository.isPhotoExists(img)
	}
	
	@SuppressLint("SimpleDateFormat")
	fun dateConvert(date: String): String {
		val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
		val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
		val photoDate: Date = inputFormat.parse(date.substring(0, 10))!!
		return outputFormat.format(photoDate)
	}
	
	fun getBitmapFromView(photo: ImageView): Bitmap? {
		val bitmap = Bitmap.createBitmap(photo.width, photo.height, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		photo.draw(canvas)
		return bitmap
	}
	
	fun getImageUri(context: Context, photo: Bitmap): Uri? {
		val bytes = ByteArrayOutputStream()
		photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
		val path =
			MediaStore.Images.Media.insertImage(context.contentResolver, photo, "Title", null)
		return Uri.parse(path)
	}
	
	fun insertDataBase(photo: UnsplashPhoto, query: String, imgDetails: ImageView, imgGone: ImageView) {
		val formattedDate = dateConvert(photo.updated_at)
		insertDatabase(
			Images(
				photo.id,
				getBitmapFromView(imgDetails)!!,
				getBitmapFromView(imgGone),
				photo.user.name,
				photo.user.username,
				photo.user.instagram_username,
				photo.user.twitter_username,
				photo.description,
				formattedDate,
				photo.color,
				photo.width,
				photo.height,
				query
			)
		)
	}
	
}