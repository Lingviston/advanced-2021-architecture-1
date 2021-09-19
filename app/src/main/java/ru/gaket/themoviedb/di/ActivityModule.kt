package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.core.navigation.NavigatorImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModule {

    @Binds
    abstract fun provideNavigator(
        impl: NavigatorImpl,
    ): Navigator
}