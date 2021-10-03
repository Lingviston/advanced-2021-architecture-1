package ru.gaket.themoviedb.presentation.movies.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.R.dimen
import ru.gaket.themoviedb.core.navigation.MovieDetailsScreen
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.databinding.MoviesFragmentBinding
import ru.gaket.themoviedb.presentation.movies.viewmodel.Loading
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.EmptyQuery
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.EmptyResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.ErrorResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult.ValidResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesViewModel
import ru.gaket.themoviedb.presentation.movies.viewmodel.Ready
import ru.gaket.themoviedb.presentation.movies.viewmodel.SearchState
import ru.gaket.themoviedb.util.afterTextChanged
import ru.gaket.themoviedb.util.hideKeyboard
import ru.gaket.themoviedb.util.toGone
import ru.gaket.themoviedb.util.toVisible
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.movies_fragment) {

    private val viewModel: MoviesViewModel by viewModels()

    private val binding by viewBinding(MoviesFragmentBinding::bind)

    private val moviesAdapter = MoviesAdapter { searchMovie ->
        navigator.navigateTo(MovieDetailsScreen(searchMovie.id, searchMovie.title))
    }

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMoviesList()
        binding.searchInput
            .afterTextChanged(viewModel::onNewQuery)

        viewModel.searchResult.observe(viewLifecycleOwner, ::handleMoviesList)
        viewModel.searchState.observe(viewLifecycleOwner, ::handleLoadingState)
    }

    private fun setupMoviesList() {
        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> 2
        }
        binding.moviesList.apply {
            layoutManager = GridLayoutManager(activity, spanCount)
            adapter = moviesAdapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount,
                    resources.getDimension(dimen.itemsDist).toInt(),
                    true
                )
            )
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == SCROLL_STATE_DRAGGING) {
                        recyclerView.hideKeyboard()
                    }
                }
            })
        }
    }

    private fun handleMoviesList(state: MoviesResult) {
        when (state) {
            is ValidResult -> {
                binding.moviesPlaceholder.toGone()
                binding.moviesList.toVisible()
                moviesAdapter.submitList(state.result)
            }
            is ErrorResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.toVisible()
                binding.moviesList.toGone()
                binding.moviesPlaceholder.setText(R.string.search_error)
                Timber.e("Something went wrong.", state.e)
            }
            is EmptyResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.toVisible()
                binding.moviesList.toGone()
                binding.moviesPlaceholder.setText(R.string.empty_result)
            }
            is EmptyQuery -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.toVisible()
                binding.moviesList.toGone()
                binding.moviesPlaceholder.setText(R.string.movies_placeholder)
            }
        }
    }

    private fun handleLoadingState(state: SearchState) {
        when (state) {
            Loading -> {
                binding.searchIcon.toGone()
                binding.searchProgress.toVisible()
            }
            Ready -> {
                binding.searchIcon.toVisible()
                binding.searchProgress.toGone()
            }
        }
    }

    companion object {

        fun newInstance(): MoviesFragment = MoviesFragment()
    }
}
