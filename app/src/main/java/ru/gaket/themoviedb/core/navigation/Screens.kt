package ru.gaket.themoviedb.core.navigation

import androidx.fragment.app.Fragment
import ru.gaket.themoviedb.presentation.auth.view.AuthFragment
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment
import ru.gaket.themoviedb.presentation.movies.view.MoviesFragment

interface Screen {

    fun destination(): Fragment
}

class MoviesScreen : Screen {

    override fun destination(): Fragment = MoviesFragment.newInstance()
}

class MovieDetailsScreen(
    private val movieId: Long,
    private val title: String,
) : Screen {

    override fun destination(): Fragment = MovieDetailsFragment.newInstance(movieId, title)
}

class AuthScreen : Screen {

    override fun destination(): Fragment = AuthFragment.newInstance()
}
