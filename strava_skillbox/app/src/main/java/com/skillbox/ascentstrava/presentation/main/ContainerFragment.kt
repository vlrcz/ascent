package com.skillbox.ascentstrava.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.data.AuthConfig
import com.skillbox.ascentstrava.databinding.FragmentContainerBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.main.di.DaggerMainComponent
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.ProfileFragmentDirections
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import javax.inject.Inject
import javax.inject.Provider

class ContainerFragment : Fragment(R.layout.fragment_container) {

    @Inject
    lateinit var viewModelProvider: Provider<MainViewModel>

    private val viewModel: MainViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private var navController: NavController? = null

    private val binding: FragmentContainerBinding by viewBinding(FragmentContainerBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerMainComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.profileFragment, R.id.activitiesFragment, R.id.shareFragment
            ) //todo разобраться как переименовать названия в тулбаре
        )

        val nestedHavHostFragment =
            childFragmentManager.findFragmentById(R.id.container) as? NavHostFragment
        navController = nestedHavHostFragment?.navController
        if (navController != null) {
            binding.bottomNavigation.setupWithNavController(navController!!)
            binding.topAppBar.setupWithNavController(navController!!, appBarConfiguration)
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.shareButton -> {
                    val url = viewModel.getProfileUrl()
                    if (url != null) {
                        val action = ProfileFragmentDirections.actionGlobalShareFragment(
                            url
                        )
                        navController?.navigate(action)
                    }
                    true
                }
                else -> false
            }
        }
    }
}