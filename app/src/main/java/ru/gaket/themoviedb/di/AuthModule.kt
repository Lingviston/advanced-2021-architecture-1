package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.auth.AuthRepositoryImpl
import ru.gaket.themoviedb.data.auth.local.AuthLocalDataSource
import ru.gaket.themoviedb.data.auth.local.AuthLocalDataSourceImpl
import ru.gaket.themoviedb.data.auth.remote.AuthRemoteDataSource
import ru.gaket.themoviedb.data.auth.remote.AuthRemoteDataSourceImpl
import ru.gaket.themoviedb.domain.auth.AuthInteractor
import ru.gaket.themoviedb.domain.auth.AuthManagerImpl
import ru.gaket.themoviedb.domain.auth.ReviewsSynchronizer
import ru.gaket.themoviedb.domain.review.ReviewsSynchronizerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindAuthLocalDataSource(
        impl: AuthLocalDataSourceImpl,
    ): AuthLocalDataSource

    @Binds
    fun bindAuthRemoteDataSource(
        impl: AuthRemoteDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    fun bindAuthInteractor(
        impl: AuthManagerImpl,
    ): AuthInteractor

    @Binds
    fun bindSyncLocalStorageUseCase(
        impl: ReviewsSynchronizerImpl,
    ): ReviewsSynchronizer
}