package ru.gaket.themoviedb.tests

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.presentation.moviedetails.view.MovieDetailsFragment

object MovieScreen : KScreen<MovieScreen>() {
    override val layoutId: Int = R.layout.fragment_movie_details
    override val viewClass: Class<*> = MovieDetailsFragment::class.java


    val title = KTextView { withId(R.id.tv_movie_details_title) }
    val description = KTextView { withId(R.id.tv_movie_details_overview) }
    val image = KImageView { withId(R.id.iv_movie_poster) }
    val loginButton = KView {
        withId(R.id.tv_review_add_label_label)
        withText(R.string.authiorize_to_add_review_label)
    }
    val addReview = KTextView {
        withId(R.id.tv_review_add_label_label)
        withText(R.string.review_add_label)
    }
}