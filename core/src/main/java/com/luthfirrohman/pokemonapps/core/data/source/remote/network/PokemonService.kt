package com.luthfirrohman.pokemonapps.core.data.source.remote.network

import com.luthfirrohman.pokemonapps.core.BuildConfig
import com.luthfirrohman.pokemonapps.core.data.source.remote.response.DetailMovieResponse
import com.luthfirrohman.pokemonapps.core.data.source.remote.response.DetailTvShowResponse
import com.luthfirrohman.pokemonapps.core.data.source.remote.response.MovieResponse
import com.luthfirrohman.pokemonapps.core.data.source.remote.response.TvShowResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("3/movie/popular")
    suspend fun getPokemonPopular(@Query("api_key") api_key: String = BuildConfig.API_TOKEN): MovieResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = BuildConfig.API_TOKEN
    ): DetailMovieResponse

    @GET("3/tv/popular")
    suspend fun getTvShowPopular(@Query("api_key") api_key: String = BuildConfig.API_TOKEN): TvShowResponse

    @GET("3/tv/{tv_id}")
    suspend fun getTvShowDetail(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") api_key: String = BuildConfig.API_TOKEN
    ): DetailTvShowResponse
}