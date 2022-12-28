package com.luthfirrohman.pokemonapps.core.domain.repository

import androidx.paging.PagedList
import com.luthfirrohman.pokemonapps.core.domain.model.Pokemon
import com.luthfirrohman.pokemonapps.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun getPopularPokemons(sort: String): Flow<Resource<PagedList<Pokemon>>>
    fun getPokemon(pokemon: String): Flow<Resource<PagedList<Pokemon>>>

//    fun getPopularTvShows(): Flow<Resource<PagedList<Pokemon>>>

    fun getDetailPokemons(movieId: String): Flow<Resource<Pokemon>>

//    fun getDetailTvShows(tvId: Int): Flow<Resource<Pokemon>>

    fun setFavoritePokemon(pokemon: Pokemon, state: Boolean)

//    fun setFavoriteTvShow(tv: Pokemon, state: Boolean)

    fun getFavoritePokemon(): Flow<PagedList<Pokemon>>

//    fun getFavoriteTvShow(): Flow<PagedList<Pokemon>>
}