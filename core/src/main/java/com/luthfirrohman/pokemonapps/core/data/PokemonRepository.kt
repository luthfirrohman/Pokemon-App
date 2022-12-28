package com.luthfirrohman.pokemonapps.core.data

import android.util.Log
import androidx.lifecycle.asFlow
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.luthfirrohman.pokemonapps.core.data.source.local.LocalDataSource
import com.luthfirrohman.pokemonapps.core.data.source.remote.ApiResponse
import com.luthfirrohman.pokemonapps.core.data.source.remote.RemoteDataSource
import com.luthfirrohman.pokemonapps.core.data.source.remote.response.*
import com.luthfirrohman.pokemonapps.core.domain.model.Pokemon
import com.luthfirrohman.pokemonapps.core.domain.repository.IMovieRepository
import com.luthfirrohman.pokemonapps.core.utils.AppExecutors
import com.luthfirrohman.pokemonapps.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) :
    IMovieRepository {

    override fun getPopularPokemons(sort: String): Flow<Resource<PagedList<Pokemon>>> {
        return object :
            NetworkBoundResource<PagedList<Pokemon>, List<ResultsItem>>() {
            override fun loadFromDB(): Flow<PagedList<Pokemon>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(
                    localDataSource.getPokemons(sort).map { DataMapper.mapEntityToDomain(it) }, config
                ).build().asFlow()
            }

            override fun shouldFetch(data: PagedList<Pokemon>?): Boolean =
                true

            override suspend fun createCall(): Flow<ApiResponse<List<ResultsItem>>> =
                remoteDataSource.getAllPokemons()

            override suspend fun saveCallResult(data: List<ResultsItem>) {
                val pokemonList = DataMapper.mapPokemonResponseToEntities(
                    data,
                    isMovie = true,
                    isTvShow = false
                )
                localDataSource.insertPokemon(pokemonList)
            }
        }.asFlow()
    }

    override fun getPokemon(pokemon: String): Flow<Resource<PagedList<Pokemon>>> {
        return object :
            NetworkBoundResource<PagedList<Pokemon>, List<ResultsItem>>() {
            override fun loadFromDB(): Flow<PagedList<Pokemon>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(
                    localDataSource.getPokemon(pokemon).map { DataMapper.mapEntityToDomain(it) }, config
                ).build().asFlow()
            }

            override fun shouldFetch(data: PagedList<Pokemon>?): Boolean =
                true

            override suspend fun createCall(): Flow<ApiResponse<List<ResultsItem>>> =
                remoteDataSource.getAllPokemons()

            override suspend fun saveCallResult(data: List<ResultsItem>) {
                val pokemonList = DataMapper.mapPokemonResponseToEntities(
                    data,
                    isMovie = true,
                    isTvShow = false
                )
                localDataSource.insertPokemon(pokemonList)
            }
        }.asFlow()

    }

    override fun getDetailPokemons(movieId: String): Flow<Resource<Pokemon>> {
        return object :
            NetworkBoundResource<Pokemon, DetailPokemonResponse>() {
            override fun loadFromDB(): Flow<Pokemon> =
                localDataSource.getDetailsPokemon(movieId).map { DataMapper.mapEntityToDomain(it) }

            override fun shouldFetch(data: Pokemon?): Boolean =
                true

            override suspend fun createCall(): Flow<ApiResponse<DetailPokemonResponse>> {
                return remoteDataSource.getDetailsPokemon(movieId)
            }

            override suspend fun saveCallResult(data: DetailPokemonResponse) {
                val pokemonDetails = DataMapper.mapDetailPokemonResponseToEntities(data)
                localDataSource.updatePokemon(pokemonDetails)
            }
        }.asFlow()
    }

    override fun getFavoritePokemon(): Flow<PagedList<Pokemon>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(
            localDataSource.getFavoritePokemon().map { DataMapper.mapEntityToDomain(it) }, config
        ).build().asFlow()
    }

    override fun setFavoritePokemon(pokemon: Pokemon, state: Boolean) {
        val pokemonData = DataMapper.mapDomainToEntity(pokemon)
        appExecutors.diskIO().execute { localDataSource.setPokemonFavorite(pokemonData, state) }
    }

}