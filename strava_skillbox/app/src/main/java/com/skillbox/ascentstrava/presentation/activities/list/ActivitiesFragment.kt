package com.skillbox.ascentstrava.presentation.activities.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.FragmentActivitiesBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.activities.list.di.DaggerActivitiesComponent
import javax.inject.Inject
import javax.inject.Provider

class ActivitiesFragment : Fragment(R.layout.fragment_activities) {

    private val binding: FragmentActivitiesBinding by viewBinding(FragmentActivitiesBinding::bind)

    @Inject
    lateinit var viewModelProvider: Provider<ActivitiesViewModel>

    private val viewModel: ActivitiesViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerActivitiesComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_activitiesFragment_to_createActivityFragment)
        }
    }
}