package com.skillbox.ascentstrava.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentContainerBinding
import com.skillbox.ascentstrava.presentation.activities.ActivitiesFragment
import com.skillbox.ascentstrava.presentation.profile.ProfileFragment

class ContainerFragment : Fragment(R.layout.fragment_container) {

    private val binding: FragmentContainerBinding by viewBinding(FragmentContainerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindBottomNavigationListener()
        binding.bottomNavigation.selectedItemId = R.id.profile
    }

    private fun bindBottomNavigationListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    setFragment(ProfileFragment())
                    true
                }
                R.id.activities -> {
                    setFragment(ActivitiesFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}