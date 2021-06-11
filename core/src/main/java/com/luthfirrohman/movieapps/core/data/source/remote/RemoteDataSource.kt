package com.luthfirrohman.movieapps.core.data.source.remote

import android.util.Log
import com.luthfirrohman.movieapps.core.data.source.remote.network.MovieService
import com.luthfirrohman.movieapps.core.data.source.remote.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val movieService: MovieService) {

    suspend fun getAllMovies(): Flow<ApiResponse<List<PopularMovieItem>>> {
        return flow {
            try {
                val response = movieService.getMoviePopular()
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllTvShows(): Flow<ApiResponse<List<PopularTvShowItem>>> {
        return flow {
            try {
                val response = movieService.getTvShowPopular()
                val dataArray = response.results
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.results))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailsMovie(movieId: Int): Flow<ApiResponse<DetailMovieResponse>> {
        return flow {
            try {
                val response = movieService.getMovieDetail(movieId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailsTvShow(tvId: Int): Flow<ApiResponse<DetailTvShowResponse>> {
        return flow {
            try {
                val response = movieService.getTvShowDetail(tvId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}