package ru.gaket.themoviedb.core.navigation

import androidx.fragment.app.Fragment
import ru.gaket.themoviedb.presentation.auth.view.AuthFragment
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment
import ru.gaket.themoviedb.presentation.movies.view.MoviesFragment

interface Screen {

    fun destination(): Fragment

    val tag: String? get() = null
}

class MoviesScreen : Screen {

    override fun destination(): Fragment = MoviesFragment.newInstance()
}

class MovieDetailsScreen(
    private val movieId: Long,
    private val title: String,
) : Screen {

    override fun destination(): Fragment = MovieDetailsFragment.newInstance(movieId, title)

    override val tag: String get() = TAG

    companion object {

        const val TAG = "MovieDetailsScreen"
    }
}

class AuthScreen : Screen {

    override fun destination(): Fragment = AuthFragment.newInstance()
}