package com.esaudev.shopapp.log_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.esaudev.shopapp.R
import com.esaudev.shopapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        subscribeViewModel()
        subscribeClickListeners()

        return binding.root
    }

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is LoginUiState.Loading -> {
                            binding.bLogIn.isEnabled = false
                            binding.bLogIn.text = ""
                            binding.bLogInProgress.visibility = View.VISIBLE
                        }

                        is LoginUiState.Idle -> {
                            binding.bLogIn.isEnabled = true
                            binding.bLogIn.text = getString(R.string.log_in_button)
                            binding.bLogInProgress.visibility = View.GONE
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { uiEvent ->
                    when(uiEvent) {
                        is LoginUiEvent.LoggedIn -> {
                            Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
                        }
                        is LoginUiEvent.Error -> {
                            Toast.makeText(context, getString(R.string.log_in_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun subscribeClickListeners() {
        binding.bLogIn.setOnClickListener {
            viewModel.logIn(
                username = binding.etUserName.text.toString(),
                password = binding.etPassword.text.toString(),
            )
        }

        binding.bSignUp.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
        }
    }
}