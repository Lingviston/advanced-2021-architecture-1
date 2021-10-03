package ru.gaket.themoviedb.presentation.auth.view

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.gaket.themoviedb.R
import ru.gaket.themoviedb.core.navigation.MovieDetailsScreen
import ru.gaket.themoviedb.core.navigation.Navigator
import ru.gaket.themoviedb.databinding.AuthFragmentBinding
import ru.gaket.themoviedb.domain.auth.LogInError
import ru.gaket.themoviedb.presentation.auth.viewmodel.AuthState
import ru.gaket.themoviedb.presentation.auth.viewmodel.AuthViewModel
import ru.gaket.themoviedb.util.clearErrorOnAnyInput
import ru.gaket.themoviedb.util.getTrimmedText
import ru.gaket.themoviedb.util.showErrorResId
import ru.gaket.themoviedb.util.showSystemMessage
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {

    private val viewModel: AuthViewModel by viewModels()

    private val binding by viewBinding(AuthFragmentBinding::bind)

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailInput.clearErrorOnAnyInput()
        binding.passwordInput.clearErrorOnAnyInput()

        binding.authBtn.setOnClickListener {
            viewModel.auth(
                email = binding.emailInput.getTrimmedText(),
                password = binding.passwordInput.getTrimmedText()
            )
        }

        viewModel.authState.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: AuthState) {
        val isAuthorizing = (state is AuthState.Authorizing)

        binding.authBtn.isEnabled = !isAuthorizing
        binding.authLoader.isVisible = isAuthorizing

        when (state) {
            AuthState.Empty,
            AuthState.Authorizing,
            -> Unit
            AuthState.Authorized -> navigator.backTo(MovieDetailsScreen.TAG)
            is AuthState.InputError -> handleInputError(state)
            is AuthState.AuthError -> showSystemMessage(text = getString(state.logInError.messageResId))
        }
    }

    private fun handleInputError(error: AuthState.InputError) =
        when (error) {
            AuthState.InputError.Email -> binding.emailInput.showErrorResId(R.string.email_input_error)
            AuthState.InputError.Password -> binding.passwordInput.showErrorResId(R.string.password_input_error)
        }

    companion object {

        fun newInstance(): AuthFragment = AuthFragment()
    }
}

@get:StringRes
private val LogInError.messageResId: Int
    get() = when (this) {
        LogInError.InvalidUserCredentials -> R.string.invalid_user_credentials
        LogInError.Unknown -> R.string.unknown_error
    }
