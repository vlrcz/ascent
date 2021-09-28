package com.skillbox.ascentstrava.presentation.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentActivitiesBinding

class ActivitiesFragment: Fragment(R.layout.fragment_activities) {

    private val binding: FragmentActivitiesBinding by viewBinding(FragmentActivitiesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}