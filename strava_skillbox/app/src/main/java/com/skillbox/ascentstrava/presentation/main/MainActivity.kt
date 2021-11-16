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
}