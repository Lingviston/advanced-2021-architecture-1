package ru.gaket.themoviedb.presentation.core

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.core.navigation.Screen
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator

    fun navigateTo(screen: Screen) {
        navigator.navigateTo(screen)
    }
}