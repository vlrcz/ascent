package com.skillbox.ascentstrava.presentation.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingModel(
    @DrawableRes val drawableRes: Int,
    @StringRes val headline2Res: Int,
    @StringRes val bodyRes: Int
)