package ru.gaket.themoviedb.tests

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.presentation.movies.view.MoviesFragment

object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int = R.layout.movies_fragment
    override val viewClass: Class<*> = MoviesFragment::class.java

    val searchInput = KEditText { withId(R.id.searchInput) }
    val searchIcon = KImageView { withId(R.id.searchIcon) }

    val moviesList = KRecyclerView({
        withId(R.id.moviesList)
    }, itemTypeBuilder = {
        itemType(::MovieItem)
    })

    class MovieItem(parent: Matcher<View>) : KRecyclerItem<MovieItem>(parent) {
        val title: KTextView = KTextView(parent) { withId(R.id.movieName) }
        val image: KImageView = KImageView(parent) { withId(R.id.movieThumbnail) }
    }
}