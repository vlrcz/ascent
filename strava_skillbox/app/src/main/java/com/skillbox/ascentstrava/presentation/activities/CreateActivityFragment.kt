package com.skillbox.ascentstrava.presentation.activities

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentCreateActivityBinding

class CreateActivityFragment : Fragment(R.layout.fragment_create_activity) {

    private val binding: FragmentCreateActivityBinding by viewBinding(FragmentCreateActivityBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}