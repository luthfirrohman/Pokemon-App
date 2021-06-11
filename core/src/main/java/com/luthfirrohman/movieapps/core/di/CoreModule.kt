package com.luthfirrohman.movieapps.core.di

import androidx.room.Room
import com.luthfirrohman.movieapps.core.data.MovieRepository
import com.luthfirrohman.movieapps.core.data.source.local.LocalDataSource
import com.luthfirrohman.movieapps.core.data.source.local.room.MovieFavoriteDatabase
import com.luthfirrohman.movieapps.core.data.source.remote.RemoteDataSource
import com.luthfirrohman.movieapps.core.data.source.remote.network.MovieService
import com.luthfirrohman.movieapps.core.domain.repository.IMovieRepository
import com.luthfirrohman.movieapps.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<MovieFavoriteDatabase>().movieDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            MovieFavoriteDatabase::class.java, "MovieApps.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(MovieService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IMovieRepository> { MovieRepository(get(), get(), get()) }
}
