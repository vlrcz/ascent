package com.skillbox.ascentstrava.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.FragmentOnboardingBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.storage.StorageViewModel
import timber.log.Timber

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val viewModel: StorageViewModel by viewModels{ ViewModelFactory { requireContext().appComponent.storageViewModel() } }

    private val binding: FragmentOnboardingBinding by viewBinding(FragmentOnboardingBinding::bind)
    private var adapter: OnboardingAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showModels(models)
        bindSkipButton()
    }

    private fun bindSkipButton() {
        binding.skipTextView.setOnClickListener {
            binding.viewPager.currentItem++
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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