package ru.gaket.themoviedb.core.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ActivityContext
import ru.gaket.themoviedb.R
import timber.log.Timber
import javax.inject.Inject

/**
 * Class responsible for the application navigation
 */
interface Navigator {

    fun navigateTo(screen: Screen)
}

class NavigatorImpl @Inject constructor(
    @ActivityContext private val context: Context,
) : Navigator {

    override fun navigateTo(screen: Screen) {
        Timber.d("Navigate to ${screen::class.simpleName}")
        getFragmentManager().beginTransaction()
            .replace(R.id.container, screen.destination())
            .commit()
    }

    private fun getFragmentManager() = (context as AppCompatActivity).supportFragmentManager
}