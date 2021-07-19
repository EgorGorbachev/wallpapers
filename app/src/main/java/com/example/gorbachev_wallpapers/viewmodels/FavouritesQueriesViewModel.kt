package com.example.gorbachev_wallpapers.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.repositories.UnsplashRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavouritesQueriesViewModel @ViewModelInject constructor(
	private val repository: UnsplashRepository
) : ViewModel() {
	
	val allData: LiveData<List<Queries>> = repository.allDataFavQueries

}