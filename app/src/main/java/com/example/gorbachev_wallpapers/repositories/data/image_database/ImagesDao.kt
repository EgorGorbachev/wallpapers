package com.example.gorbachev_wallpapers.repositories.data.image_database

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.models.Queries

@Dao
interface ImagesDao {
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun add(Image: Images)
	
	@Query("SELECT * FROM images_table ORDER BY id ASC")
	fun readAllData(): LiveData<List<Images>>
	
	@androidx.room.Query("SELECT EXISTS (SELECT 1 FROM images_table WHERE img = :img)")
	fun isPhotoExists(img: Bitmap): Boolean
	
	@Delete
	suspend fun delete(image: Images)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun add(query: Queries)
	
	@Query("SELECT * FROM queries_table")
	fun readAllDataQuery(): LiveData<List<Queries>>
	
	@androidx.room.Query("SELECT EXISTS (SELECT 1 FROM queries_table WHERE `query` = :query)")
	fun isQueryExists(query: String): Boolean
	
	@Delete
	suspend fun delete(query: Queries)
	
	@Update
	suspend fun update(query: Queries)
	
	@Query("SELECT * FROM queries_table WHERE `like`=1")
	fun readAllFavouritesQueries(): LiveData<List<Queries>>?
}