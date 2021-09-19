package ru.gaket.themoviedb.core

import ru.gaket.themoviedb.BuildConfig
import javax.inject.Inject

interface BuildConfigProvider {

    val debug: Boolean
    val appId: String
    val buildType: String
    val flavor: String
    val versionCode: Int
    val versionName: String
    val baseUrl: String
    val apiKey: String
}

class BuildConfigProviderImpl @Inject constructor() : BuildConfigProvider {

    override val debug: Boolean get() = BuildConfig.DEBUG
    override val appId: String get() = BuildConfig.APPLICATION_ID
    override val buildType: String get() = BuildConfig.BUILD_TYPE
    override val flavor: String get() = BuildConfig.FLAVOR
    override val versionCode: Int get() = BuildConfig.VERSION_CODE
    override val versionName: String get() = BuildConfig.VERSION_NAME
    override val baseUrl: String get() = BuildConfig.BASE_URL
    override val apiKey: String get() = BuildConfig.THE_MOVIE_DB_API_KEY
}