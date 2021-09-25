package com.skillbox.ascentstrava.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding: FragmentOnboardingBinding by viewBinding(FragmentOnboardingBinding::bind)
    private var adapter: OnboardingAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showModels(models)
        bindSkipButton()
    }

    private fun bindSkipButton() {

        binding.skipTextView.setOnClickListener {
            if (binding.skipTextView.text == getString(R.string.skip_btn)) {
                binding.viewPager.currentItem++
            } else {
                findNavController().navigate(R.id.action_onboardingFragment_to_authFragment)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var isLastPageSwiped = false
            private var counterPageScroll = 0

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == models.size - 1 && positionOffset == 0f && !isLastPageSwiped) {
                    if (counterPageScroll != 0) {
                        isLastPageSwiped = true;
                        findNavController().navigate(R.id.action_onboardingFragment_to_authFragment)
                    }
                    counterPageScroll++;
                } else {
                    counterPageScroll = 0;
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == models.size - 1) {
                    binding.skipTextView.text = getString(R.string.okay_btn)
                } else {
                    binding.skipTextView.text = getString(R.string.skip_btn)
                }
            }
        })
    }

    private fun showModels(models: List<OnboardingModel>) {
        adapter = OnboardingAdapter(models, this)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

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
}