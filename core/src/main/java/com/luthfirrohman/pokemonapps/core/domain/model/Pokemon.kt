package com.luthfirrohman.pokemonapps.core.domain.model

data class Movie(
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