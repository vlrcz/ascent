package com.skillbox.ascentstrava.presentation.auth

import androidx.lifecycle.ViewModel
import com.skillbox.ascentstrava.data.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

}