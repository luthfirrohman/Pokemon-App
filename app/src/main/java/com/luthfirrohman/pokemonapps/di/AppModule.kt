package com.luthfirrohman.movieapps.di

import com.luthfirrohman.movieapps.core.domain.usecase.MovieInteractor
import com.luthfirrohman.movieapps.core.domain.usecase.MovieUseCase
import com.luthfirrohman.movieapps.detail.DetailViewModel
import com.luthfirrohman.movieapps.movie.MovieViewModel
import com.luthfirrohman.movieapps.tvshow.TvShowViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { TvShowViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}