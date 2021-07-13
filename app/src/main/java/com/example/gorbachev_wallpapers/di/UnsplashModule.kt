package com.example.gorbachev_wallpapers.di

import android.content.Context
import android.provider.MediaStore
import androidx.room.Room
import com.example.gorbachev_wallpapers.api.UnsplashApi
import com.example.gorbachev_wallpapers.repositories.data.image_database.ImagesDao
import com.example.gorbachev_wallpapers.repositories.data.image_database.ImagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
	
	@Singleton
	@Provides
	fun provideSearchDatabase(@ApplicationContext context : Context) =
		Room.databaseBuilder(
			context.applicationContext,
			ImagesDatabase::class.java,
			"images_database"
		).build()
	
	@Provides
	fun provideSearchDAO(appDatabase: ImagesDatabase): ImagesDao {
		return appDatabase.ImagesDao()
	}
}