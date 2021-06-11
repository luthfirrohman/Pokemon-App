package com.luthfirrohman.movieapps.core.data

import androidx.lifecycle.asFlow
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.luthfirrohman.movieapps.core.data.source.local.LocalDataSource
import com.luthfirrohman.movieapps.core.data.source.remote.ApiResponse
import com.luthfirrohman.movieapps.core.data.source.remote.RemoteDataSource
import com.luthfirrohman.movieapps.core.data.source.remote.response.DetailMovieResponse
import com.luthfirrohman.movieapps.core.data.source.remote.response.DetailTvShowResponse
import com.luthfirrohman.movieapps.core.data.source.remote.response.PopularMovieItem
import com.luthfirrohman.movieapps.core.data.source.remote.response.PopularTvShowItem
import com.luthfirrohman.movieapps.core.domain.model.Movie
import com.luthfirrohman.movieapps.core.domain.repository.IMovieRepository
import com.luthfirrohman.movieapps.core.utils.AppExecutors
import com.luthfirrohman.movieapps.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) :
    IMovieRepository {

    override fun getPopularMovies(): Flow<Resource<PagedList<Movie>>> {
        return object :
            com.luthfirrohman.movieapps.core.data.NetworkBoundResource<PagedList<Movie>, List<PopularMovieItem>>() {
            override fun loadFromDB(): Flow<PagedList<Movie>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(
                    localDataSource.getMovies().map { DataMapper.mapEntityToDomain(it) }, config
                ).build().asFlow()
            }

            override fun shouldFetch(data: PagedList<Movie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<PopularMovieItem>>> =
                remoteDataSource.getAllMovies()

            override suspend fun saveCallResult(data: List<PopularMovieItem>) {
                val movieList = DataMapper.mapMovieResponseToEntities(
                    data,
                    isMovie = true,
                    isTvShow = false
                )
                localDataSource.insertMovie(movieList)
            }
        }.asFlow()
    }

    override fun getPopularTvShows(): Flow<Resource<PagedList<Movie>>> {
        return object :
            com.luthfirrohman.movieapps.core.data.NetworkBoundResource<PagedList<Movie>, List<PopularTvShowItem>>() {
            override fun loadFromDB(): Flow<PagedList<Movie>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(
                    localDataSource.getTvShows().map { DataMapper.mapEntityToDomain(it) }, config
                ).build().asFlow()
            }

            override fun shouldFetch(data: PagedList<Movie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<PopularTvShowItem>>> =
                remoteDataSource.getAllTvShows()

            override suspend fun saveCallResult(data: List<PopularTvShowItem>) {
                val tvShowList = DataMapper.mapTvShowResponseToEntities(
                    data,
                    isMovie = false,
                    isTvShow = true
                )
                localDataSource.insertMovie(tvShowList)
            }
        }.asFlow()
    }

    override fun getDetailMovies(movieId: Int): Flow<Resource<Movie>> {
        return object :
            com.luthfirrohman.movieapps.core.data.NetworkBoundResource<Movie, DetailMovieResponse>() {
            override fun loadFromDB(): Flow<Movie> =
                localDataSource.getDetailsMovie(movieId).map { DataMapper.mapEntityToDomain(it) }

            override fun shouldFetch(data: Movie?): Boolean =
                data?.tagline.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<DetailMovieResponse>> =
                remoteDataSource.getDetailsMovie(movieId)

            override suspend fun saveCallResult(data: DetailMovieResponse) {
                val movieDetails = DataMapper.mapDetailMovieResponseToEntities(data)
                localDataSource.updateMovie(movieDetails)
            }
        }.asFlow()
    }

    override fun getDetailTvShows(tvId: Int): Flow<Resource<Movie>> {
        return object :
            com.luthfirrohman.movieapps.core.data.NetworkBoundResource<Movie, DetailTvShowResponse>() {
            override fun loadFromDB(): Flow<Movie> =
                localDataSource.getDetailsTvShow(tvId).map { DataMapper.mapEntityToDomain(it) }

            override fun shouldFetch(data: Movie?): Boolean =
                data?.tagline.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<DetailTvShowResponse>> =
                remoteDataSource.getDetailsTvShow(tvId)

            override suspend fun saveCallResult(data: DetailTvShowResponse) {
                val tvShowDetails = DataMapper.mapDetailTvShowResponseToEntities(data)
                localDataSource.updateMovie(tvShowDetails)
            }
        }.asFlow()
    }

    override fun getFavoriteMovie(): Flow<PagedList<Movie>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(
            localDataSource.getFavoriteMovie().map { DataMapper.mapEntityToDomain(it) }, config
        ).build().asFlow()
    }

    override fun getFavoriteTvShow(): Flow<PagedList<Movie>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(
            localDataSource.getFavoriteTvShow().map { DataMapper.mapEntityToDomain(it) }, config
        ).build().asFlow()
    }

    override fun setFavoriteMovie(movie: Movie, state: Boolean) {
        val movieData = DataMapper.mapDomainToEntity(movie)
        appExecutors.diskIO().execute { localDataSource.setMovieFavorite(movieData, state) }
    }

    override fun setFavoriteTvShow(tv: Movie, state: Boolean) {
        val tvShowData = DataMapper.mapDomainToEntity(tv)
        appExecutors.diskIO().execute { localDataSource.setMovieFavorite(tvShowData, state) }
    }

}