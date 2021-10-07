package com.skillbox.ascentstrava.presentation.profile

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.databinding.FragmentProfileBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.profile.data.UpdateRequestBody
import com.skillbox.ascentstrava.presentation.profile.di.DaggerProfileComponent
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var viewModelProvider: Provider<ProfileViewModel>

    @Inject
    lateinit var authManager: AuthManager

    private val viewModel: ProfileViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shareBtn.setOnClickListener {
            authManager.brokeAccessToken() //todo удалить
        }

        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.getProfileInfo()

        viewModel.athlete.observe(viewLifecycleOwner) { athlete ->
            bindProfileInfo(athlete)
            bindWeightView(athlete)
        }

        viewModel.error.observe(viewLifecycleOwner) {t ->
            t.message?.let { toast(it) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindProfileInfo(athlete: Athlete) {
        binding.nameTextView.text = athlete.firstName + " " + athlete.lastName
        binding.usernameTextView.text = "@" + athlete.userName
        binding.followersCountTextView.text = athlete.followers?.toString() ?: "0"
        binding.followingCountTextView.text = athlete.friends?.toString() ?: "0"
        when (athlete.gender) {
            "M" -> binding.genderValue.text = "Male"
            "F" -> binding.genderValue.text = "Female"
        }
        binding.countryValue.text = athlete.country

        Glide.with(this)
            .load(athlete.photoUrl)
            .into(binding.imageView)
    }

    @SuppressLint("SetTextI18n")
    private fun bindWeightView(
        athlete: Athlete
    ) {
        val list = (0..200).toList().map {
            "$it kg"
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list)
        binding.autoCompleteTextView.setAdapter(adapter)

        if (athlete.weight != null) {
            binding.autoCompleteTextView.setText(
                athlete.weight.toInt().toString() + " kg", false
            )
        } else {
            binding.autoCompleteTextView.setText("Вес не выбран", false)
        }

        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val newWeight = adapter.getItem(position)?.substringBefore(" kg")?.toFloat()!!
            viewModel.changeAthleteWeight(UpdateRequestBody(newWeight))
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}