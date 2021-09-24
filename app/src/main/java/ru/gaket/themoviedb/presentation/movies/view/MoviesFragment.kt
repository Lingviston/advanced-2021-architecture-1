package ru.gaket.themoviedb.presentation.movies.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.MovieDetailsScreen
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.databinding.MoviesFragmentBinding
import ru.gaket.themoviedb.presentation.movies.viewmodel.EmptyQuery
import ru.gaket.themoviedb.presentation.movies.viewmodel.EmptyResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.ErrorResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.Loading
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesResult
import ru.gaket.themoviedb.presentation.movies.viewmodel.MoviesViewModel
import ru.gaket.themoviedb.presentation.movies.viewmodel.Ready
import ru.gaket.themoviedb.presentation.movies.viewmodel.SearchState
import ru.gaket.themoviedb.presentation.movies.viewmodel.TerminalError
import ru.gaket.themoviedb.presentation.movies.viewmodel.ValidResult
import ru.gaket.themoviedb.util.afterTextChanged
import ru.gaket.themoviedb.util.hideKeyboard
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

        binding.moviesList.apply {
            val spanCount = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> 4
                else -> 2
            }
            layoutManager = GridLayoutManager(activity, spanCount)
            adapter = moviesAdapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    spanCount,
                    resources.getDimension(R.dimen.itemsDist).toInt(),
                    true
                )
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == SCROLL_STATE_DRAGGING) {
                        recyclerView.hideKeyboard()
                    }
                }
            })
        }

        if (savedInstanceState == null) {
            // todo: [Sergey] check if we need this scoping
            lifecycleScope.launch {
                viewModel.queryChannel.send("")
            }
        }
        binding.searchInput.afterTextChanged {
            lifecycleScope.launch {
                viewModel.queryChannel.send(it.toString())
            }
        }

        viewModel.searchResult.observe(viewLifecycleOwner, ::handleMoviesList)
        viewModel.searchState.observe(viewLifecycleOwner, ::handleLoadingState)
    }

    private fun handleLoadingState(state: SearchState) {
        when (state) {
            Loading -> {
                binding.searchIcon.visibility = View.GONE
                binding.searchProgress.visibility = View.VISIBLE
            }
            Ready -> {
                binding.searchIcon.visibility = View.VISIBLE
                binding.searchProgress.visibility = View.GONE
            }
        }
    }

    private fun handleMoviesList(state: MoviesResult) {
        when (state) {
            is ValidResult -> {
                binding.moviesPlaceholder.visibility = View.GONE
                binding.moviesList.visibility = View.VISIBLE
                moviesAdapter.submitList(state.result)
            }
            is ErrorResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.VISIBLE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.search_error)
                Timber.e("Something went wrong.", state.e)
            }
            is EmptyResult -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.VISIBLE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.empty_result)
            }
            is EmptyQuery -> {
                moviesAdapter.submitList(emptyList())
                binding.moviesPlaceholder.visibility = View.VISIBLE
                binding.moviesList.visibility = View.GONE
                binding.moviesPlaceholder.setText(R.string.movies_placeholder)
            }
            is TerminalError -> {
                // Something went terribly wrong!
                println("Our Flow terminated unexpectedly, so we're bailing!")
                Toast.makeText(
                    activity,
                    getString(R.string.error_unknown_on_download),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {

        fun newInstance(): MoviesFragment = MoviesFragment()
    }
}
