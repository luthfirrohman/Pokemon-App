package com.luthfirrohman.movieapps.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailTvShowResponse(

    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("first_air_date")
    val firstAirDate: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("poster_path")
    val posterPath: String? = null,

    @field:SerializedName("vote_average")
    val voteAverage: Double? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("tagline")
    val tagline: String? = null,

    @field:SerializedName("episode_run_time")
    val episodeRunTime: List<Int?>,

    )