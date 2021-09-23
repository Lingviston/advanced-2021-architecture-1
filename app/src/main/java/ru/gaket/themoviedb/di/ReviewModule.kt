package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.presentation.review.ReviewWizard
import ru.gaket.themoviedb.presentation.review.ReviewWizardImplementation
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReviewModule {

    @Singleton
    @Binds
    fun bindsReviewWizard(wizard: ReviewWizardImplementation): ReviewWizard
}