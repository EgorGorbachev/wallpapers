package com.example.gorbachev_wallpapers.repositories.data.image_database

import android.media.Image
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gorbachev_wallpapers.models.Images

@Dao
interface ImagesDao {
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun add(Image: Images)
	
	@Query("SELECT * FROM images_table ORDER BY id ASC")
	fun readAllData(): LiveData<List<Images>>
	
	@Delete
	suspend fun delete(image:Images)
}