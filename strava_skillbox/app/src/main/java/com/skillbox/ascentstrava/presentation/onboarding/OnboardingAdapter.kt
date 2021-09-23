package com.skillbox.ascentstrava.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(
    private val models: List<OnboardingModel>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return models.size
    }

    override fun createFragment(position: Int): Fragment {
        val model = models[position]
        return OnboardingFragment.newInstance(
            model.drawableRes,
            model.headline2Res,
            model.bodyRes
        )
    }
}