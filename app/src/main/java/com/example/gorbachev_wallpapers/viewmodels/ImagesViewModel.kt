package com.example.gorbachev_wallpapers.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.repositories.UnsplashRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImagesViewModel @ViewModelInject constructor(
	private val repository: UnsplashRepository
) : ViewModel() {
	
	val allData:LiveData<List<Images>> =repository.allDataImages
	
	fun insertDatabase(image:Images ){
		GlobalScope.launch(Dispatchers.Default) {
			if (!isPhotoExists(image.img)){
				repository.add(image)
			} else Log.d("duplicate","duplicate" )
		}
	}
	
	fun isPhotoExists(img: Bitmap):Boolean{
		return repository.isPhotoExists(img)
	}
	
}