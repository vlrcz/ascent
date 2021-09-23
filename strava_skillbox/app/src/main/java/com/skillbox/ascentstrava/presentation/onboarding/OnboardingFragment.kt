package com.skillbox.ascentstrava.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding: FragmentOnboardingBinding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onboardingImageView.setImageResource(requireArguments().getInt(KEY_IMAGE))
        binding.headTextView.setText(requireArguments().getInt(KEY_HEADLINE))
        binding.bodyTextView.setText(requireArguments().getInt(KEY_BODY))
    }

    companion object {
        private const val KEY_IMAGE = "image"
        private const val KEY_HEADLINE = "headline"
        private const val KEY_BODY = "body"

        fun newInstance(
            @DrawableRes drawableRes: Int,
            @StringRes headline2Res: Int,
            @StringRes bodyRes: Int
        ): OnboardingFragment {
            return OnboardingFragment().apply {
                val args = Bundle()
                arguments = args
                args.putInt(KEY_IMAGE, drawableRes)
                args.putInt(KEY_HEADLINE, headline2Res)
                args.putInt(KEY_BODY, bodyRes)
            }
        }
    }
}