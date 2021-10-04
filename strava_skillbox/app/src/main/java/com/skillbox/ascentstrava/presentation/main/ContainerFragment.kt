package com.skillbox.ascentstrava.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentContainerBinding

class ContainerFragment : Fragment(R.layout.fragment_container) {

    private var navController: NavController? = null
    private val binding: FragmentContainerBinding by viewBinding(FragmentContainerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedHavHostFragment = childFragmentManager.findFragmentById(R.id.container) as? NavHostFragment
        navController = nestedHavHostFragment?.navController
        if (navController != null) {
            binding.bottomNavigation.setupWithNavController(navController!!)
        }
    }
}