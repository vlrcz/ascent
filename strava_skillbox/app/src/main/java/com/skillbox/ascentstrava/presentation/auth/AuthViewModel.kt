package com.skillbox.ascentstrava.presentation.auth

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.data.AuthRepository
import com.skillbox.ascentstrava.utils.SingleLiveEvent
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authService: AuthorizationService
) : ViewModel() {

    private val openAuthPageLiveEvent = SingleLiveEvent<Intent>()
    private val toastLiveEvent = SingleLiveEvent<Int>()
    private val loadingMutableLiveData = MutableLiveData(false)
    private val authSuccessLiveEvent = SingleLiveEvent<Unit>()

    val openAuthPageLiveData: LiveData<Intent>
        get() = openAuthPageLiveEvent

    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData

    val toastLiveData: LiveData<Int>
        get() = toastLiveEvent

    val authSuccessLiveData: LiveData<Unit>
        get() = authSuccessLiveEvent

    fun onAuthCodeFailed(exception: AuthorizationException) {
        toastLiveEvent.postValue(R.string.auth_canceled)
    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        loadingMutableLiveData.postValue(true)
        authRepository.performTokenRequest(
            authService = authService,
            tokenRequest = tokenRequest,
            onComplete = {
                loadingMutableLiveData.postValue(false)
                authSuccessLiveEvent.postValue(Unit)
            },
            onError = {
                loadingMutableLiveData.postValue(false)
                toastLiveEvent.postValue(R.string.auth_canceled)
            }
        )
    }

    fun openLoginPage() {
        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
            authRepository.getAuthRequest()
        )
        openAuthPageLiveEvent.postValue(openAuthPageIntent)
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}