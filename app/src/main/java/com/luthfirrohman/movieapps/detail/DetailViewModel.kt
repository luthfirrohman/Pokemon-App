package com.luthfirrohman.movieapps.detail

import androidx.lifecycle.*
import com.luthfirrohman.movieapps.core.domain.model.Movie
import com.luthfirrohman.movieapps.core.domain.usecase.MovieUseCase
import com.luthfirrohman.movieapps.core.data.Resource

class DetailViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {

    private var movieId = MutableLiveData<Int>()
    private var tvId = MutableLiveData<Int>()

    fun setSelectedMovie(movieId: Int) {
        this.movieId.value = movieId
    }

    fun setSelectedTvShow(tvId: Int) {
        this.tvId.value = tvId
    }

    var detailMovie: LiveData<Resource<Movie>> = Transformations.switchMap(movieId) {
        movieUseCase.getDetailMovies(it).asLiveData()
    }

    var detailTvShow: LiveData<Resource<Movie>> = Transformations.switchMap(tvId) {
        movieUseCase.getDetailTvShows(it).asLiveData()
    }

    fun setFavoriteMovie() {
        val detailMovie = detailMovie.value
        if (detailMovie != null) {
            val movie = detailMovie.data
            if (movie != null) {
                val newState = !movie.isFavorite
                movieUseCase.setFavoriteMovie(movie, newState)
            }
        }
    }

    fun setFavoriteTvShow() {
        val detailTvShow = detailTvShow.value
        if (detailTvShow != null) {
            val tvShow = detailTvShow.data
            if (tvShow != null) {
                val newState = !tvShow.isFavorite
                movieUseCase.setFavoriteTvShow(tvShow, newState)
            }
        }
    }

}
