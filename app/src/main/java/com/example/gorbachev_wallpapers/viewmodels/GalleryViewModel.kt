package com.example.gorbachev_wallpapers.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.gorbachev_wallpapers.repositories.UnsplashRepository


class GalleryViewModel @ViewModelInject constructor(
	private val repository: UnsplashRepository
): ViewModel() {
	
	private val currentQuery = MutableLiveData(DEFAULT_QUERY)
	
	val photos = currentQuery.switchMap { queryString ->
		repository.getSearchResults(queryString).cachedIn(viewModelScope)
	
	}
	
	fun searchPhotos(query: String){
		currentQuery.value = query
	}
	
	fun getCurrentQuery():String{
		return currentQuery.value!!
	}
	
	companion object {
		private const val DEFAULT_QUERY = "Forest"
	}

}