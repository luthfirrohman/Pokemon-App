package com.luthfirrohman.movieapps.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import com.luthfirrohman.movieapps.core.domain.model.Movie
import com.luthfirrohman.movieapps.core.domain.usecase.MovieUseCase
import com.luthfirrohman.movieapps.core.data.Resource

class TvShowViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {

    fun getTvShows(): LiveData<Resource<PagedList<Movie>>> =
        movieUseCase.getPopularTvShows().asLiveData()
}