package com.example.gorbachev_wallpapers.api

import com.example.gorbachev_wallpapers.models.UnsplashPhoto

data class UnsplashResponse(
	val total: Int,
	val results: List<UnsplashPhoto>
)