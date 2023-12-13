package com.esaudev.shopapp.ui.sign_up

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.esaudev.shopapp.R
import com.esaudev.shopapp.databinding.FragmentSignUpBinding
import com.esaudev.shopapp.domain.model.User
import com.esaudev.shopapp.domain.usecase.EMPTY_EMAIL_ERROR
import com.esaudev.shopapp.domain.usecase.EMPTY_PASSWORD_ERROR
import com.esaudev.shopapp.domain.usecase.EMPTY_USERNAME_ERROR
import com.esaudev.shopapp.domain.usecase.PASSWORDS_NOT_MATCH_ERROR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment: Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding
        get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        subscribeViewModel()
        subscribeClickListeners()
        subscribePasswordValidator()

        return binding.root
    }

    private fun subscribePasswordValidator() {
        binding.etPassword.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                binding.gPasswordRequirements.visibility = View.VISIBLE
            } else {
                binding.gPasswordRequirements.visibility = View.GONE
            }

            viewModel.onPasswordsChanged(
                password = it.toString(),
                confPassword = binding.etConfPassword.text.toString()
            )
        }

        binding.etConfPassword.addTextChangedListener {
            viewModel.onPasswordsChanged(
                password = binding.etPassword.text.toString(),
                confPassword = it.toString()
            )
        }
    }

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { resultEvent ->
                    when (resultEvent) {
                        is SignUpUiEvent.AccountCreated -> {
                            activity?.onBackPressedDispatcher?.onBackPressed()
                            Toast.makeText(context, R.string.account_created_successfully, Toast.LENGTH_SHORT).show()
                        }

                        is SignUpUiEvent.Error -> {
                            when (resultEvent.errorCode) {
                                EMPTY_EMAIL_ERROR -> Toast.makeText(
                                    context,
                                    R.string.error__empty_email,
                                    Toast.LENGTH_SHORT
                                ).show()

                                EMPTY_PASSWORD_ERROR -> Toast.makeText(
                                    context,
                                    R.string.error__empty_password,
                                    Toast.LENGTH_SHORT
                                ).show()

                                EMPTY_USERNAME_ERROR -> Toast.makeText(
                                    context,
                                    R.string.error__empty_name,
                                    Toast.LENGTH_SHORT
                                ).show()

                                PASSWORDS_NOT_MATCH_ERROR -> Toast.makeText(
                                    context,
                                    R.string.error__passwords_not_match,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when(uiState) {
                        is SignUpUiState.Idle -> {
                            binding.bSignUp.isEnabled = true
                            binding.bSignUp.text = getString(R.string.sign_up_button)
                            binding.bSignUpProgress.visibility = View.GONE
                        }
                        is SignUpUiState.Loading -> {
                            binding.bSignUp.isEnabled = false
                            binding.bSignUp.text = ""
                            binding.bSignUpProgress.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.passwordValidatorState.collect {
                    binding.bSignUp.isEnabled = it.allRequirementsMet()
                    Log.d("Test", it.allRequirementsMet().toString())
                    if (it.passwordHasNumber) {
                        binding.ivPasswordHasNumber.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                        binding.tvPasswordHasNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    } else {
                        binding.ivPasswordHasNumber.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
                        binding.tvPasswordHasNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }

                    if (it.passwordLength) {
                        binding.ivPasswordLength.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                        binding.tvPasswordLength.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    } else {
                        binding.ivPasswordLength.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
                        binding.tvPasswordLength.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }

                    if (it.passwordsMatch) {
                        binding.ivPasswordMatch.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
                        binding.tvPasswordMatch.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    } else {
                        binding.ivPasswordMatch.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
                        binding.tvPasswordMatch.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                }
            }
        }
    }

    private fun subscribeClickListeners() {
        binding.bSignUp.setOnClickListener {
            viewModel.signUp(
                user = User(
                    userName = binding.etUserName.text.toString(),
                    firstName = binding.etFirstName.text.toString(),
                    lastName = binding.etLastName.text.toString(),
                    email = binding.etEmail.text.toString()
                ),
                password = binding.etPassword.text.toString(),
                confPassword = binding.etConfPassword.text.toString()
            )
        }

        binding.bBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }


}