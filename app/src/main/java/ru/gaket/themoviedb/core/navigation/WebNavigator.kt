package ru.gaket.themoviedb.core.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.gaket.themoviedb.di.BrowseMovieBaseUrlQualifier
import javax.inject.Inject

/**
 * Class responsible for the browser navigation
 */
interface WebNavigator {

    fun navigateTo(movieId: Int): Boolean
}

class WebNavigatorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @BrowseMovieBaseUrlQualifier private val browseMovieUrl: String,
) : WebNavigator {

    /**
     * Open a browser for a given url
     *
     * @return [true] if navigation succeded, [false] otherwise
     */
    override fun navigateTo(movieId: Int): Boolean {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("${browseMovieUrl}${movieId}")
        )
        browserIntent.flags = browserIntent.flags or Intent.FLAG_ACTIVITY_NEW_TASK
        return try {
            context.startActivity(browserIntent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }
}