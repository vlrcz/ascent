package com.skillbox.ascentstrava.presentation.activities.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.data.db.ActivityEntity
import com.skillbox.ascentstrava.databinding.FragmentCreateActivityBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.network.ConnectionManager
import com.skillbox.ascentstrava.presentation.activities.create.di.DaggerCreateActivityComponent
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.presentation.activities.data.ActivityType
import com.skillbox.ascentstrava.utils.toast
import java.text.SimpleDateFormat
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
        viewModel.saveSuccessLiveData.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
            viewModel.updateEntityByUniqueId(it.first, it.second)
        }
        viewModel.insertSuccessLiveData.observe(viewLifecycleOwner) {
            if (!ConnectionManager.isOnline(requireContext())) {
                findNavController().popBackStack()
            }
        }
        viewModel.saveErrorLiveData.observe(viewLifecycleOwner) {
            viewModel.deleteEntityByUniqueId(it)
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { toast(it) }
        viewModel.toastLiveData.observe(viewLifecycleOwner) { toast(it) }
    }

    private fun createActivity() {
        val name = binding.nameEditText.text?.toString()
        val type = binding.typeEditText.text?.toString()
        val date = binding.dateEditText.text?.toString()
        val elapsedTime = binding.timeEditText.text?.toString()
        val distance = binding.distanceEditText.text?.toString()
        val description = binding.descriptionEditText.text?.toString().orEmpty()
        if (name.isNullOrBlank() || type.isNullOrBlank() || date.isNullOrBlank() || elapsedTime.isNullOrBlank() || distance.isNullOrBlank()) {
            toast(getString(R.string.incorrect_form)) //todo добавить обводку форм красным цветом
        } else {
            val uniqueId = UUID.randomUUID().toString()
            val activityModel = ActivityModel(
                stravaId = null,
                activityName = name,
                activityType = type,
                startedAt = date,
                elapsedTime = elapsedTime.toInt(),
                distance = distance.toFloat(),
                description = description
            )
            val activityEntity = ActivityEntity(
                id = uniqueId,
                activityName = name,
                activityType = type,
                startedAt = date,
                elapsedTime = elapsedTime.toInt(),
                distance = distance.toFloat(),
                description = description,
                isPending = true
            )
            viewModel.createActivityModel(activityModel, uniqueId)
            viewModel.insertActivityEntityToDb(activityEntity)
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