package com.skillbox.ascentstrava.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.data.AuthConfig
import com.skillbox.ascentstrava.databinding.FragmentContainerBinding
import com.skillbox.ascentstrava.presentation.profile.Athlete
import com.skillbox.ascentstrava.presentation.profile.ProfileFragmentDirections
import com.skillbox.ascentstrava.presentation.profile.data.AthleteManager
import javax.inject.Inject

class ContainerFragment : Fragment(R.layout.fragment_container) {

    @Inject
    lateinit var athleteManager: AthleteManager

    private var navController: NavController? = null

    private val binding: FragmentContainerBinding by viewBinding(FragmentContainerBinding::bind)

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
                    if (athleteManager.getProfileUrl() != null) {
                        val action = ProfileFragmentDirections.actionGlobalShareFragment(
                            athleteManager.getProfileUrl()!!
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