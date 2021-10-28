package ru.gaket.themoviedb.tests

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.presentation.movies.view.MoviesFragment

object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int
        get() = R.layout.movies_fragment
    override val viewClass: Class<*>
        get() = MoviesFragment::class.java

    val searchInput = KEditText { withId(R.id.searchInput) }
    val searchIcon = KImageView { withId(R.id.searchIcon) }
}