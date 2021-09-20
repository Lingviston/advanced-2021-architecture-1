package ru.gaket.themoviedb.core.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ActivityContext
import ru.gaket.themoviedb.R
import timber.log.Timber
import javax.inject.Inject


// todo: [pash & Sergey] Refactor Navigator
/**
 * Class responsible for the application navigation
 *
 * Can't inject this router into ViewModel: [https://github.com/google/dagger/issues/2698]
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
