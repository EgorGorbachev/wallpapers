package com.example.gorbachev_wallpapers.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "queries_table")
data class Queries(
	@PrimaryKey
	val query: String,
	var like: Boolean,
	val queryCount: Int,
	val time: String
) : Parcelable
