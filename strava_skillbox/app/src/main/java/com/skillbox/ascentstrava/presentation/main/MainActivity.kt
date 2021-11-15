package com.skillbox.ascentstrava.presentation.main

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.utils.setAppLocale

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.data != null) {
            intent.dataString?.let { url ->
                val action = WebViewFragmentDirections.actionGlobalWebViewFragment(url)
                findNavController(R.id.fragment).navigate(action)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences =
            newBase?.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val language = sharedPreferences?.getString(LOCALE, null)
        super.attachBaseContext(ContextWrapper(language?.let { newBase.setAppLocale(it) }))
    }

    companion object {
        private const val SHARED_PREFS_NAME = "shared_prefs"
        private const val LOCALE = "Locale"
    }
}