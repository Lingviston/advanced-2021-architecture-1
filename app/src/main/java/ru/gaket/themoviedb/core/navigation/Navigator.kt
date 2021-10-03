package ru.gaket.themoviedb.core.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
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

    fun navigateTo(
        screen: Screen,
        addToBackStack: Boolean = true,
    )

    fun backTo(tag: String)

    fun back()
}

class NavigatorImpl @Inject constructor(
    @ActivityContext private val context: Context,
) : Navigator {

    override fun navigateTo(screen: Screen, addToBackStack: Boolean) {
        Timber.d("Navigate to ${screen::class.simpleName}")
        getFragmentManager().commit {
            replace(R.id.container, screen.destination(), screen.tag)
            if (addToBackStack) {
                addToBackStack(screen.tag)
            }
        }
    }

    override fun backTo(tag: String) {
        Timber.d("Navigate Back to $tag")
        getFragmentManager().popBackStack(tag, 0)
    }

    override fun back() {
        getFragmentManager().popBackStackImmediate()
    }

    private fun getFragmentManager() = (context as AppCompatActivity).supportFragmentManager
}
