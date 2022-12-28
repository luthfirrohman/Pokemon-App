package com.luthfirrohman.movieapps.core.data.source.local

import androidx.paging.DataSource
import com.luthfirrohman.movieapps.core.data.source.local.entity.MoviesDataEntity
import com.luthfirrohman.movieapps.core.data.source.local.room.MovieFavoriteDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val movieDao: MovieFavoriteDao) {

    suspend fun insertMovie(movie: List<MoviesDataEntity>) {
        movieDao.insertMovies(movie)
    }

    suspend fun updateMovie(movie: MoviesDataEntity) {
        movieDao.insertDetails(movie)
    }

    fun getMovies(): DataSource.Factory<Int, MoviesDataEntity> =
        movieDao.getMovies()

    fun getTvShows(): DataSource.Factory<Int, MoviesDataEntity> =
        movieDao.getTvShows()

    fun getDetailsMovie(movieId: Int): Flow<MoviesDataEntity> =
        movieDao.getDetailsMovie(movieId)

    fun getDetailsTvShow(tvId: Int): Flow<MoviesDataEntity> =
        movieDao.getDetailsTvShow(tvId)

    fun getFavoriteMovie(): DataSource.Factory<Int, MoviesDataEntity> =
        movieDao.getFavoriteMovie()

    fun getFavoriteTvShow(): DataSource.Factory<Int, MoviesDataEntity> =
        movieDao.getFavoriteTvShow()

    fun setMovieFavorite(movie: MoviesDataEntity, state: Boolean) {
        movie.isFavorite = state
        movieDao.updateMovies(movie)
    }

}