package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import ru.gaket.themoviedb.presentation.MainActivity
import ru.gaket.themoviedb.presentation.review.ReviewNavigator
import ru.gaket.themoviedb.presentation.review.ReviewNavigatorImplementation
import javax.inject.Singleton

@Module
@InstallIn(MainActivity::class)
interface ReviewModule {

    @Singleton
    @Binds
    fun provideReviewNavigator(navigator: ReviewNavigatorImplementation): ReviewNavigator

}