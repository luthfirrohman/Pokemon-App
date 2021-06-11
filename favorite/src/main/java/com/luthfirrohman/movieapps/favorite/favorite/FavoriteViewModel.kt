package com.luthfirrohman.movieapps.favorite.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import com.luthfirrohman.movieapps.core.domain.model.Movie
import com.luthfirrohman.movieapps.core.domain.usecase.MovieUseCase

class FavoriteViewModel(private val movieUseCase: MovieUseCase) :
    ViewModel() {

    fun getFavoriteMovies(): LiveData<PagedList<Movie>> =
        movieUseCase.getFavoriteMovie().asLiveData()

    fun getFavoriteTvShows(): LiveData<PagedList<Movie>> =
        movieUseCase.getFavoriteTvShow().asLiveData()

    fun setFavoriteMovie(moviesDataEntity: Movie) {
        val newState = !moviesDataEntity.isFavorite
        movieUseCase.setFavoriteMovie(moviesDataEntity, newState)
    }

    fun setFavoriteTvShow(moviesDataEntity: Movie) {
        val newState = !moviesDataEntity.isFavorite
        movieUseCase.setFavoriteTvShow(moviesDataEntity, newState)
    }
}