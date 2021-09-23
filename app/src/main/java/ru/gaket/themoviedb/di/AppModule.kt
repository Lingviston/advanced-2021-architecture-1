package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.core.BuildConfigProvider
import ru.gaket.themoviedb.core.BuildConfigProviderImpl
import ru.gaket.themoviedb.core.navigation.WebNavigator
import ru.gaket.themoviedb.core.navigation.WebNavigatorImpl
import ru.gaket.themoviedb.data.auth.AuthRepository
import ru.gaket.themoviedb.data.auth.AuthRepositoryImpl
import ru.gaket.themoviedb.data.auth.local.AuthLocalDataSource
import ru.gaket.themoviedb.data.auth.local.AuthLocalDataSourceImpl
import ru.gaket.themoviedb.data.auth.remote.AuthRemoteDataSource
import ru.gaket.themoviedb.data.auth.remote.AuthRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindBuildConfigProvider(
        impl: BuildConfigProviderImpl,
    ): BuildConfigProvider

    @Binds
    fun bindWebNavigator(
        impl: WebNavigatorImpl,
    ): WebNavigator

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
}