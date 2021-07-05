package com.example.gorbachev_wallpapers.api

import com.example.gorbachev_wallpapers.models.UnsplashPhoto

data class UnsplashResponse (
	val results: List<UnsplashPhoto>
)