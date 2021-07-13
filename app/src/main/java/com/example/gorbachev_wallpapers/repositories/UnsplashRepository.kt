package com.example.gorbachev_wallpapers.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.gorbachev_wallpapers.api.UnsplashApi
import com.example.gorbachev_wallpapers.api.UnsplashPagingSource
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.repositories.data.image_database.ImagesDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi, private val imagesDao: ImagesDao) {
	
	val allData:LiveData<List<Images>> =imagesDao.readAllData()
	
	fun getSearchResults(query:String) =
		Pager(
			config = PagingConfig(
				pageSize = 20,
				maxSize = 100,
				enablePlaceholders = false
			),
			pagingSourceFactory = {UnsplashPagingSource(unsplashApi,query)}
		).liveData
	
	suspend fun add(image: Images) {
		imagesDao.add(image)
	}
	
	suspend fun delete(image: Images) {
		imagesDao.delete(image)
	}
	
}
