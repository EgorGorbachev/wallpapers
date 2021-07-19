package com.example.gorbachev_wallpapers.models

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.io.ByteArrayOutputStream

@Parcelize
@Entity(tableName = "images_table")
data class Images(
	@PrimaryKey()
	val id: String,
	val img: Bitmap,
	val imageProfile:Bitmap?,
	val name: String,
	val username: String,
	val instagram_username: String?,
	val twitter_username: String?,
	val description: String?,
	val date: String?,
	val color: String?,
	val width: Int?,
	val height: Int?,
	val query: String
): Parcelable

