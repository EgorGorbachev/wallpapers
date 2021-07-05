package com.example.gorbachev_wallpapers.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class UnsplashPhoto(
	val id: String,
	val description: String?,
	val urls: UnsolashPhotoUrls,
	val user: UnsplashUser
):Parcelable {
	
	@Parcelize
	data class UnsolashPhotoUrls(
		val raw:String,
		val full:String,
		val regular:String,
		val small:String,
		val thumb:String
	):Parcelable
	
	@Parcelize
	data class UnsplashUser(
		val name: String,
		val username: String
	): Parcelable{
		val attributionUrl get() = "https://unsplash.com/$username?utm_source=Gorbachev_wallpaper&utm_medium=referral"
	}
}