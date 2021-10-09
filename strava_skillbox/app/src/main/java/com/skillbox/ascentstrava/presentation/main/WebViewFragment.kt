package com.skillbox.ascentstrava.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentWebviewBinding

class WebViewFragment(val url: String) : Fragment(R.layout.fragment_webview) {

    private val binding: FragmentWebviewBinding by viewBinding(FragmentWebviewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}