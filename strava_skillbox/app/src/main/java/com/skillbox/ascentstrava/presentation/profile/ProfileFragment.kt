package com.skillbox.ascentstrava.presentation.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.databinding.FragmentProfileBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.profile.di.DaggerProfileComponent
import javax.inject.Inject
import javax.inject.Provider

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var viewModelProvider: Provider<ProfileViewModel>

    @Inject
    lateinit var authManager: AuthManager

    private val viewModel: ProfileViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfileInfo()

        binding.testTextView.setOnClickListener {
            authManager.brokeAccessToken() //TODO()
        }

        viewModel.athlete.observe(viewLifecycleOwner) {
            binding.testTextView.text = """
                ${it.userName}
                ${it.lastName}
                ${it.firstName}
            """.trimIndent()
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.testTextView.text = it.message
        }
    }
}