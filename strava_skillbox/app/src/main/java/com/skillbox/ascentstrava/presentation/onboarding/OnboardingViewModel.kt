package com.skillbox.ascentstrava.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.ascentstrava.presentation.onboarding.data.OnboardingRepository
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val isFirstTimeEntry = MutableLiveData<Boolean>()

    val isFirstEntry: LiveData<Boolean>
        get() = isFirstTimeEntry
}