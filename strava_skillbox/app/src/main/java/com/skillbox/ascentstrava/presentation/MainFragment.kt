package com.skillbox.ascentstrava.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentMainBinding
import com.skillbox.ascentstrava.presentation.onboarding.OnboardingAdapter
import com.skillbox.ascentstrava.presentation.onboarding.OnboardingModel

class MainFragment: Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private val models = listOf(
        OnboardingModel(
            R.drawable.humaaans,
            R.string.welcome_to_ascent,
            R.string.welcome_text
        ),
        OnboardingModel(
            R.drawable.friends,
            R.string.friends,
            R.string.friends_text
        ),
        OnboardingModel(
            R.drawable.activities,
            R.string.activities,
            R.string.activities_text
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showModels(models)
    }

    private fun showModels(models: List<OnboardingModel>) {
        val adapter = OnboardingAdapter(models, this)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }
}