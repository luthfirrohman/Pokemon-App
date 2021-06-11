package com.luthfirrohman.movieapps.core.data.source.local.room

import androidx.paging.DataSource
import androidx.room.*
import com.luthfirrohman.movieapps.core.data.source.local.entity.MoviesDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = MoviesDataEntity::class)
    suspend fun insertMovies(movies: List<MoviesDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = MoviesDataEntity::class)
    suspend fun insertDetails(movies: MoviesDataEntity)

    @Update(entity = MoviesDataEntity::class)
    fun updateMovies(movies: MoviesDataEntity)


    @Query("SELECT * FROM movie_table WHERE isMovie = 1")
    fun getMovies(): DataSource.Factory<Int, MoviesDataEntity>

    @Query("SELECT * FROM movie_table WHERE isTvShow = 1")
    fun getTvShows(): DataSource.Factory<Int, MoviesDataEntity>

    @Query("SELECT * FROM movie_table WHERE id = :movieId")
    fun getDetailsMovie(movieId: Int): Flow<MoviesDataEntity>

    @Query("SELECT * FROM movie_table WHERE id = :tvId")
    fun getDetailsTvShow(tvId: Int): Flow<MoviesDataEntity>


    @Query("SELECT * FROM movie_table WHERE isMovie = 1 and isFavorite = 1")
    fun getFavoriteMovie(): DataSource.Factory<Int, MoviesDataEntity>

    @Query("SELECT * FROM movie_table WHERE isTvShow = 1 and isFavorite = 1")
    fun getFavoriteTvShow(): DataSource.Factory<Int, MoviesDataEntity>

}