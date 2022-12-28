package com.luthfirrohman.pokemonapps.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import com.luthfirrohman.pokemonapps.core.domain.model.Movie
import com.luthfirrohman.pokemonapps.core.domain.usecase.MovieUseCase
import com.luthfirrohman.pokemonapps.core.data.Resource

class MovieViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {

    fun getMovies(): LiveData<Resource<PagedList<Movie>>> =
        movieUseCase.getPopularMovies().asLiveData()
}