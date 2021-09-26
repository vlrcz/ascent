package com.skillbox.ascentstrava.presentation.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.FragmentAuthBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.auth.di.DaggerAuthComponent
import javax.inject.Inject
import javax.inject.Provider

class AuthFragment : Fragment(R.layout.fragment_auth) {

    @Inject
    lateinit var viewModelProvider: Provider<AuthViewModel>

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

    }
}