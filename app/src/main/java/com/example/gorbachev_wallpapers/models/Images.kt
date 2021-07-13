package com.example.gorbachev_wallpapers.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

@Entity(tableName = "images_table")
data class Images(
	@PrimaryKey(autoGenerate = true)
	val id: Int,
	val img: Bitmap,
	val lol: String
)

