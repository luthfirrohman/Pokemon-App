package com.luthfirrohman.movieapps.core.domain.repository

import androidx.paging.PagedList
import com.luthfirrohman.movieapps.core.domain.model.Movie
import com.luthfirrohman.movieapps.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getPopularMovies(): Flow<Resource<PagedList<Movie>>>

    fun getPopularTvShows(): Flow<Resource<PagedList<Movie>>>

    fun getDetailMovies(movieId: Int): Flow<Resource<Movie>>

    fun getDetailTvShows(tvId: Int): Flow<Resource<Movie>>

    fun setFavoriteMovie(movie: Movie, state: Boolean)

    fun setFavoriteTvShow(tv: Movie, state: Boolean)

    fun getFavoriteMovie(): Flow<PagedList<Movie>>

    fun getFavoriteTvShow(): Flow<PagedList<Movie>>
}