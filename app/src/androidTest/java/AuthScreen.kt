package ru.gaket.themoviedb.tests

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KTextInputLayout
import io.github.kakaocup.kakao.text.KButton
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.presentation.auth.view.AuthFragment

object AuthScreen : KScreen<AuthScreen>() {
    override val layoutId: Int = R.layout.auth_fragment
    override val viewClass: Class<*> = AuthFragment::class.java

    val emailInput = KTextInputLayout { withId(R.id.email_input) }
    val passwordInput = KTextInputLayout { withId(R.id.password_input) }

    val authButton = KButton { withId(R.id.auth_btn) }
}