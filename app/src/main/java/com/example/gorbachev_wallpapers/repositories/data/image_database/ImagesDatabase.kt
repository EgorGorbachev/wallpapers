package com.example.gorbachev_wallpapers.repositories.data.image_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gorbachev_wallpapers.models.Images
import com.example.gorbachev_wallpapers.presentation.util.Converters

@Database(entities = [Images::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ImagesDatabase : RoomDatabase() {
	
	abstract fun ImagesDao(): ImagesDao
}