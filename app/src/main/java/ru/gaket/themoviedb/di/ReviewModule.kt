package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gaket.themoviedb.domain.review.model.ReviewFormModel
import ru.gaket.themoviedb.domain.review.repository.ReviewRepository
import ru.gaket.themoviedb.domain.review.repository.ReviewRepositoryImpl
import ru.gaket.themoviedb.domain.review.store.ItemStore
import ru.gaket.themoviedb.domain.review.store.ReviewStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReviewModule {

    @Singleton
    @Binds
    fun bindsReviewRepository(repository: ReviewRepositoryImpl): ReviewRepository

    @Singleton
    @Binds
    fun bindsReviewStore(store: ReviewStore): ItemStore<ReviewFormModel>
}