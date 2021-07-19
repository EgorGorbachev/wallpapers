package com.example.gorbachev_wallpapers.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.repositories.UnsplashRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class QueriesViewModel @ViewModelInject constructor(
	private val repository: UnsplashRepository
) : ViewModel() {
	
	val allData: LiveData<List<Queries>> = repository.allDataQueries
	
	fun insertDatabase(query: Queries) {
		GlobalScope.launch(Dispatchers.Default) {
			if (!isQueryExists(query.query)) {
				repository.add(query)
			} else Log.d("duplicate", "duplicate")
		}
	}
	
	private fun isQueryExists(query: String): Boolean {
		return repository.isQueryExists(query)
	}
	
	private fun update(query: Queries){
		viewModelScope.launch(Dispatchers.IO) {
			repository.update(query)
		}
	}
	
}