package com.example.gorbachev_wallpapers.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class UnsplashPhoto(
	val id: String,
	val description: String?,
	val urls: UnsplashPhotoUrls,
	val user: UnsplashUser,
	val width: Int,
	val height: Int,
	val updated_at: String,
	val color: String,
):Parcelable {
	
	@Parcelize
	data class UnsplashPhotoUrls(
		val raw:String,
		val full:String,
		val regular:String,
		val small:String,
		val thumb:String,
	):Parcelable
	
	@Parcelize
	data class UnsplashUser(
		val portfolio_url: String,
		val name: String,
		val username: String,
		val instagram_username: String,
		val twitter_username: String,
		val profile_image: ProfileImg,
		
	): Parcelable{
		@Parcelize
		data class ProfileImg(
			val small: String,
			val medium: String,
			val large: String
		):Parcelable
		val attributionUrl get() = "https://unsplash.com/$username?utm_source=Gorbachev_wallpaper&utm_medium=referral"
	}
}