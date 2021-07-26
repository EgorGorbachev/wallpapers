package com.example.gorbachev_wallpapers.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.repositories.UnsplashRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.io.IOException

@DelicateCoroutinesApi
class GalleryViewModel @ViewModelInject constructor(
	private val repository: UnsplashRepository
) : ViewModel() {
	
	private val currentQuery = MutableLiveData(DEFAULT_QUERY)
	
	val photos = currentQuery.switchMap { queryString ->
		repository.getSearchResults(queryString).cachedIn(viewModelScope)
	}
	
	suspend fun getTotal(query: String) = repository.getTotal(query)
	
	fun insertDatabase(query: Queries) {
		GlobalScope.launch(Dispatchers.Default) {
			repository.add(query)
		}
	}
	
	fun searchPhotos(query: String) {
		currentQuery.value = query
	}
	
	fun getCurrentQuery(): String {
		return currentQuery.value!!
	}
	
	companion object {
		private const val DEFAULT_QUERY = "Forest"
	}
	
	fun isOnline(): Boolean {
		val runtime = Runtime.getRuntime()
		try {
			val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
			val exitValue = ipProcess.waitFor()
			return exitValue == 0
		} catch (e: IOException) {
			e.printStackTrace()
		} catch (e: InterruptedException) {
			e.printStackTrace()
		}
		return false
	}
	
	fun insertQueryInDatabase(query: String, like: Boolean) {
		GlobalScope.launch(Dispatchers.Main) {
			insertDatabase(
				Queries(query, like, getTotal(query), currentTime())
			)
		}
	}
	
	private fun currentTime(): String {
		return DateTime.now().toString()
	}
	
}