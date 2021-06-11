package com.luthfirrohman.movieapps.core.domain.usecase

import androidx.paging.PagedList
import com.luthfirrohman.movieapps.core.domain.model.Movie
import com.luthfirrohman.movieapps.core.domain.repository.IMovieRepository
import com.luthfirrohman.movieapps.core.data.Resource
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val iMovieRepository: IMovieRepository) : MovieUseCase {
    override fun getPopularMovies(): Flow<Resource<PagedList<Movie>>> =
        iMovieRepository.getPopularMovies()

    override fun getPopularTvShows(): Flow<Resource<PagedList<Movie>>> =
        iMovieRepository.getPopularTvShows()

    override fun getDetailMovies(movieId: Int): Flow<Resource<Movie>> =
        iMovieRepository.getDetailMovies(movieId)

    override fun getDetailTvShows(tvId: Int): Flow<Resource<Movie>> =
        iMovieRepository.getDetailTvShows(tvId)

    override fun setFavoriteMovie(movie: Movie, state: Boolean) =
        iMovieRepository.setFavoriteMovie(movie, state)

    override fun setFavoriteTvShow(tv: Movie, state: Boolean) =
        iMovieRepository.setFavoriteTvShow(tv, state)

    override fun getFavoriteMovie(): Flow<PagedList<Movie>> = iMovieRepository.getFavoriteMovie()

    override fun getFavoriteTvShow(): Flow<PagedList<Movie>> = iMovieRepository.getFavoriteTvShow()
}