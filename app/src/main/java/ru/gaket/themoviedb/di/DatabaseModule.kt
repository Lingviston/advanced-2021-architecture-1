package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.data.core.db.MoviesDbClient
import ru.gaket.themoviedb.data.core.db.MoviesDbClientImpl
import ru.gaket.themoviedb.data.genres.local.GenresDao
import ru.gaket.themoviedb.data.movies.local.MoviesDao
import ru.gaket.themoviedb.data.review.local.MyReviewsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {

    @Binds
    @Singleton
    fun bindMoviesDbClient(
        impl: MoviesDbClientImpl,
    ): MoviesDbClient
}

@Module
@InstallIn(SingletonComponent::class)
object DaoWrapperModule {

    @Provides
    fun provideGenresDao(client: MoviesDbClient): GenresDao = client.genresDao()

    @Provides
    fun provideMoviesDao(client: MoviesDbClient): MoviesDao = client.moviesDao()

    @Provides
    fun provideMyReviewsDao(client: MoviesDbClient): MyReviewsDao = client.myReviewsDao()
}