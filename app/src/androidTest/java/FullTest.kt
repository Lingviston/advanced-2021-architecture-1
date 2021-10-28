package ru.gaket.themoviedb.tests

import androidx.test.rule.ActivityTestRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.domain.auth.AuthInteractor
import ru.gaket.themoviedb.presentation.MainActivity

@HiltAndroidTest
class FullTest : TestCase() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Inject
    lateinit var authInteractor: AuthInteractor

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test() =
        before { runBlocking { authInteractor.logOut() } }
            .after { }
            .run {
                step("Type Duna, press Find") {
                    activityTestRule.launchActivity(null)
                    MainScreen {
                        searchInput {
                            isVisible()
                            typeText("Duna")
                        }
                        searchIcon {
                            isVisible()
                            click()
                        }
                    }
                }
                step("Open Duna film details") {
                    MainScreen {
                        moviesList {
                            isVisible()
                            firstChild<MainScreen.MovieItem> {
                                isVisible()
                                title { hasText("Duna") }
                                click()
                            }
                        }
                    }
                }
                step("Check film details screen") {
                    MovieScreen {
                        title {
                            isVisible()
                            hasText("Duna")
                        }
                        description {
                            isVisible()
                        }
                        image {
                            isVisible()
                        }
                    }
                }
                step("Open Login") {
                    MovieScreen {
                        loginButton {
                            isVisible()
                            click()
                        }
                    }
                }
                step("Login") {
                    AuthScreen {
                        emailInput.edit.typeText("test")
                        authButton.click()
                        emailInput.hasError(R.string.email_input_error)
                        emailInput.edit.clearText()
                        emailInput.edit.typeText("test@test.com")
                        authButton.click()
                        passwordInput.hasError(R.string.password_input_error)
                        passwordInput.edit.typeText("Password")
                        authButton.click()
                    }
                }
                step("Check film details screen contains `Add review`") {
                    MovieScreen {
                        addReview {
                            isVisible()
                            containsText("Add review")
                        }
                    }
                }
            }
}

