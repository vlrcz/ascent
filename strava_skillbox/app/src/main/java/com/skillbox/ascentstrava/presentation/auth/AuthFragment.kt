package com.skillbox.ascentstrava.presentation.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.FragmentAuthBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.auth.di.DaggerAuthComponent
import com.skillbox.ascentstrava.utils.toast
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

class AuthFragment : Fragment(R.layout.fragment_auth) {

    @Inject
    lateinit var viewModelProvider: Provider<AuthViewModel>

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val tokenExchangeRequest = result.data?.let {
                    AuthorizationResponse.fromIntent(it)
                        ?.createTokenExchangeRequest()
                }
                val exception = AuthorizationException.fromIntent(result.data)
                when {
                    tokenExchangeRequest != null && exception == null ->
                        viewModel.onAuthCodeReceived(tokenExchangeRequest)
                    exception != null -> viewModel.onAuthCodeFailed(exception)
                }
            } else {
                toast(R.string.call_canceled)
            }
        }

    private val binding: FragmentAuthBinding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel: AuthViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerAuthComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() {
        binding.authBtn.setOnClickListener { viewModel.openLoginPage() }
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::updateIsLoading)
        viewModel.openAuthPageLiveData.observe(viewLifecycleOwner) { intent ->
            openAuthPage(intent)
        }
        viewModel.toastLiveData.observe(viewLifecycleOwner) {
            toast(it)
        }
        viewModel.authSuccessLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_authFragment_to_containerFragment)
        }
    }

    private fun updateIsLoading(isLoading: Boolean) {
        binding.authBtn.isVisible = !isLoading
        binding.alreadyMemberTextView.isVisible = !isLoading
        binding.lineImageView1.isVisible = !isLoading
        binding.lineImageView2.isVisible = !isLoading
        binding.authProgress.isVisible = isLoading
    }

    private fun openAuthPage(intent: Intent) {
        activityResultLauncher.launch(intent)
    }
}