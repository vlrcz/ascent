package com.skillbox.ascentstrava.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.isServerError(): Boolean {
    return this is HttpException && this.code() >= 500
}

fun Throwable.isNetworkError(): Boolean {
    return this is UnknownHostException || this is ConnectException || this is SocketTimeoutException
}

fun Fragment.toast(@StringRes stringRes: Int) {
    Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}