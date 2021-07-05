package com.example.gorbachev_wallpapers.di

import com.example.gorbachev_wallpapers.api.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UnsplashModule {
	
	@Provides
	@Singleton
	fun provideRetrofit(): Retrofit =
		Retrofit.Builder()
			.baseUrl(UnsplashApi.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build()

	@Provides
	@Singleton
	fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi =
		retrofit.create(UnsplashApi::class.java)
}