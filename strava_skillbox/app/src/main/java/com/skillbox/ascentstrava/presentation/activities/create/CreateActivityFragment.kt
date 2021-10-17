package com.skillbox.ascentstrava.presentation.activities.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.FragmentCreateActivityBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.activities.create.di.DaggerCreateActivityComponent
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.presentation.activities.data.ActivityType
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

import javax.inject.Inject
import javax.inject.Provider

class CreateActivityFragment : Fragment(R.layout.fragment_create_activity) {

    private val binding: FragmentCreateActivityBinding by viewBinding(FragmentCreateActivityBinding::bind)

    @Inject
    lateinit var viewModelProvider: Provider<CreateActivityViewModel>

    private val viewModel: CreateActivityViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerCreateActivityComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindTypeView()
        bindViewModel()

        binding.addBtn.setOnClickListener {
            createActivity()
        }
        binding.dateEditText.setOnClickListener {
            initStartedAtTimePicker()
        }
    }

    private fun bindViewModel() {
        viewModel.saveSuccessLiveData.observe(viewLifecycleOwner) { findNavController().popBackStack() }
        viewModel.saveErrorLiveData.observe(viewLifecycleOwner) { toast(it) }
    }

    private fun createActivity() {
        val name = binding.nameEditText.text?.toString().orEmpty()
        val type = binding.typeEditText.text?.toString().orEmpty()
        val date = binding.dateEditText.text?.toString().orEmpty()
        val elapsedTime = binding.timeEditText.text?.toString().orEmpty()
        val distance = binding.distanceEditText.text?.toString().orEmpty()
        val description = binding.descriptionEditText.text?.toString().orEmpty()
        val activity = ActivityModel(
            id = null,
            activityName = name,
            activityType = type,
            startedAt = date,
            elapsedTime = elapsedTime.toInt(),
            distance = distance.toFloat(),
            description = description
        )
        viewModel.createActivity(activity)
    }

    private fun initStartedAtTimePicker() {
        val currentDateTime = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        val formatter =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
                        currentDateTime.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                        binding.dateEditText.setText(formatter.format(currentDateTime.time))
                    },
                    currentDateTime.get(Calendar.HOUR_OF_DAY),
                    currentDateTime.get(Calendar.MINUTE),
                    true
                )
                    .show()
            },
            currentDateTime.get(Calendar.YEAR),
            currentDateTime.get(Calendar.MONTH),
            currentDateTime.get(Calendar.DAY_OF_MONTH)
        )
            .show()
    }

    private fun bindTypeView() {
        val list = ActivityType.values()
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list)
        binding.typeEditText.setAdapter(adapter)
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}