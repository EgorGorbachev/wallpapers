package com.example.gorbachev_wallpapers.repositories.data.image_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.models.Queries
import com.example.gorbachev_wallpapers.presentation.util.Converters

@Database(entities = arrayOf(Images::class, Queries::class), version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ImagesDatabase : RoomDatabase() {
	
	abstract fun ImagesDao(): ImagesDao
}