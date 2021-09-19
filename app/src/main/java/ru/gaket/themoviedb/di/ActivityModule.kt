package ru.gaket.themoviedb.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.core.navigation.NavigatorImpl

@Module
@InstallIn(ActivityComponent::class)
interface ActivityModule {

    @Binds
    fun provideNavigator(
        impl: NavigatorImpl,
    ): Navigator
}