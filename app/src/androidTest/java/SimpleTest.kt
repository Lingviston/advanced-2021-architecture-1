package ru.gaket.themoviedb.tests

import androidx.test.rule.ActivityTestRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import ru.gaket.themoviedb.presentation.MainActivity

class SimpleTest : TestCase() {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun test() =
        run {
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
        }
}

