package com.luthfirrohman.movieapps.core.utils

import com.luthfirrohman.movieapps.core.data.source.local.entity.MoviesDataEntity
import com.luthfirrohman.movieapps.core.data.source.remote.response.DetailMovieResponse
import com.luthfirrohman.movieapps.core.data.source.remote.response.DetailTvShowResponse
import com.luthfirrohman.movieapps.core.data.source.remote.response.PopularMovieItem
import com.luthfirrohman.movieapps.core.data.source.remote.response.PopularTvShowItem
import com.luthfirrohman.movieapps.core.domain.model.Movie

object DataMapper {
    fun mapMovieResponseToEntities(
        input: List<PopularMovieItem>,
        isMovie: Boolean,
        isTvShow: Boolean
    ): List<MoviesDataEntity> {
        val movieList = ArrayList<MoviesDataEntity>()

        input.map {
            val tourism = MoviesDataEntity(
                id = it.id,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath,
                title = it.title,
                tagline = null,
                runtime = 0,
                voteAverage = it.voteAverage,
                releaseDate = it.releaseDate,
                overview = it.overview,
                isMovie = isMovie,
                isTvShow = isTvShow
            )
            movieList.add(tourism)
        }
        return movieList
    }

    fun mapDetailMovieResponseToEntities(input: DetailMovieResponse): MoviesDataEntity {
        return MoviesDataEntity(
            id = input.id,
            posterPath = input.posterPath,
            backdropPath = input.backdropPath,
            title = input.title,
            tagline = input.tagline,
            runtime = input.runtime,
            voteAverage = input.voteAverage,
            releaseDate = input.releaseDate,
            overview = input.overview,
            isMovie = true,
            isTvShow = false,
            isFavorite = false
        )
    }

    fun mapTvShowResponseToEntities(
        input: List<PopularTvShowItem>,
        isMovie: Boolean,
        isTvShow: Boolean
    ): List<MoviesDataEntity> {
        val movieList = ArrayList<MoviesDataEntity>()
        input.map {
            val tourism = MoviesDataEntity(
                id = it.id,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath,
                title = it.name,
                tagline = null,
                runtime = 0,
                voteAverage = it.voteAverage,
                releaseDate = it.firstAirDate,
                overview = it.overview,
                isMovie = isMovie,
                isTvShow = isTvShow
            )
            movieList.add(tourism)
        }
        return movieList
    }

    fun mapDetailTvShowResponseToEntities(input: DetailTvShowResponse): MoviesDataEntity {
        val episodeRuntime =
            if (input.episodeRunTime.toString() == "[]") {
                0
            } else {
                input.episodeRunTime.first()?.toInt()
            }
        return MoviesDataEntity(
            id = input.id,
            posterPath = input.posterPath,
            backdropPath = input.backdropPath,
            title = input.name,
            tagline = input.tagline,
            runtime = episodeRuntime,
            voteAverage = input.voteAverage,
            releaseDate = input.firstAirDate,
            overview = input.overview,
            isMovie = false,
            isTvShow = true,
            isFavorite = false
        )
    }

    fun mapEntityToDomain(input: MoviesDataEntity): Movie {
        return Movie(
            id = input.id,
            posterPath = input.posterPath,
            backdropPath = input.backdropPath,
            title = input.title,
            tagline = input.tagline,
            runtime = input.runtime,
            voteAverage = input.voteAverage,
            releaseDate = input.releaseDate,
            overview = input.overview,
            isMovie = input.isMovie,
            isTvShow = input.isTvShow,
            isFavorite = input.isFavorite
        )
    }

    fun mapDomainToEntity(input: Movie) = MoviesDataEntity(
        id = input.id,
        posterPath = input.posterPath,
        backdropPath = input.backdropPath,
        title = input.title,
        tagline = input.tagline,
        runtime = input.runtime,
        voteAverage = input.voteAverage,
        releaseDate = input.releaseDate,
        overview = input.overview,
        isMovie = input.isMovie,
        isTvShow = input.isTvShow,
        isFavorite = input.isFavorite

    )
}