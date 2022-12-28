package com.luthfirrohman.pokemonapps.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MoviesDataEntity(
    @PrimaryKey
    @NonNull
    val id: Int?,
    val posterPath: String?,
    val backdropPath: String?,
    val title: String?,
    var tagline: String?,
    var runtime: Int?,
    val voteAverage: Double?,
    val releaseDate: String?,
    val overview: String?,
    var isMovie: Boolean = false,
    var isTvShow: Boolean = false,
    var isFavorite: Boolean = false
)
