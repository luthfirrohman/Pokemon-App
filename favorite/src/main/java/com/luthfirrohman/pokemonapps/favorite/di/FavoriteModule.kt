package com.luthfirrohman.movieapps.favorite.di

import com.luthfirrohman.movieapps.favorite.favorite.FavoriteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    viewModel { FavoriteViewModel(get()) }
}
