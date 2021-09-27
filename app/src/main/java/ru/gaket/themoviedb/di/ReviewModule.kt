package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.domain.review.ReviewRepository
import ru.gaket.themoviedb.domain.review.ReviewRepositoryImplementation
import ru.gaket.themoviedb.domain.store.ItemStore
import ru.gaket.themoviedb.domain.store.ReviewFormModel
import ru.gaket.themoviedb.domain.store.ReviewStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReviewModule {

    @Singleton
    @Binds
    fun bindsReviewRepository(repository: ReviewRepositoryImplementation): ReviewRepository

    @Singleton
    @Binds
    fun bindsReviewStore(store: ReviewStore): ItemStore<ReviewFormModel>
}