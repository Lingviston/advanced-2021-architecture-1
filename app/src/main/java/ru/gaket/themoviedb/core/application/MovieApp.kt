package ru.gaket.themoviedb.core.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.gaket.themoviedb.core.BuildConfigProvider
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MovieApp : Application() {

    @Inject
    lateinit var buildConfig: BuildConfigProvider

    override fun onCreate() {
        super.onCreate()
        if (buildConfig.debug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}