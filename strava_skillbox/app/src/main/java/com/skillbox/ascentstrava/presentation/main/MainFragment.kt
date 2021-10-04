package com.skillbox.ascentstrava.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.auth.di.DaggerAuthComponent
import com.skillbox.ascentstrava.presentation.main.di.DaggerMainComponent
import javax.inject.Inject
import javax.inject.Provider

class MainFragment : Fragment(R.layout.fragment_main) {

    @Inject
    lateinit var viewModelProvider: Provider<MainViewModel>

    private val viewModel: MainViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerMainComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isFirstEntry.observe(viewLifecycleOwner) { isFirstEntry ->
            when (isFirstEntry) {
                true -> {
                    if (viewModel.containsAccessToken()) {
                        findNavController().navigate(R.id.action_mainFragment_to_containerFragment)
                    } else {
                        findNavController().navigate(R.id.action_mainFragment_to_authFragment)
                    }
                }
                else -> {
                    findNavController().navigate(R.id.action_mainFragment_to_onboardingFragment)
                    viewModel.addFlagAfterFirstEntry()
                }
            }
        }
    }
}