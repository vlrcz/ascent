package com.skillbox.ascentstrava.presentation.activities.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.FragmentCreateActivityBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.activities.create.di.DaggerCreateActivityComponent
import com.skillbox.ascentstrava.presentation.activities.data.ActivityType
import com.skillbox.ascentstrava.utils.toast
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject
import javax.inject.Provider

class CreateActivityFragment : Fragment(R.layout.fragment_create_activity) {

    companion object {
        private const val REFRESH = "refresh_after_create"
    }

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
        viewModel.successLiveData.observe(viewLifecycleOwner) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(REFRESH, true)
            findNavController().popBackStack()
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            toast(it)
        }
    }

    private fun createActivity() {
        val name = binding.nameEditText.text?.toString()
        val type = binding.typeEditText.text?.toString()
        val date = binding.dateEditText.text?.toString()
        val elapsedTime = binding.timeEditText.text?.toString()
        val distance = binding.distanceEditText.text?.toString()
        val description = binding.descriptionEditText.text?.toString().orEmpty()
        if (name.isNullOrBlank() || type.isNullOrBlank() || date.isNullOrBlank() || elapsedTime.isNullOrBlank() || distance.isNullOrBlank()) {
            toast(getString(R.string.incorrect_form))
        } else {
            viewModel.createActivity(
                name,
                type,
                date,
                elapsedTime.toInt(),
                distance.toFloat(),
                description
            )
        }
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
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ROOT)
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
}