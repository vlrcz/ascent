package com.skillbox.ascentstrava.presentation.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.databinding.FragmentProfileBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.network.ConnectionManager
import com.skillbox.ascentstrava.presentation.profile.data.UpdateRequestBody
import com.skillbox.ascentstrava.presentation.profile.di.DaggerProfileComponent
import javax.inject.Inject
import javax.inject.Provider


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        private const val MALE = "Male"
        private const val FEMALE = "Female"
        private const val OTHER = "Other"
        private const val KG = "kg"
    }

    @Inject
    lateinit var viewModelProvider: Provider<ProfileViewModel>

    @Inject
    lateinit var authManager: AuthManager // todo удалить

    private val viewModel: ProfileViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private var logoutDialog: AlertDialog? = null

    private var navController: NavController? = null

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logoutDialog?.dismiss()
        logoutDialog = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shareBtn.setOnClickListener {
            val url = viewModel.getProfileUrl()
            if (url != null) {
                val action = ProfileFragmentDirections.actionGlobalShareFragment(
                    url
                )
                findNavController().navigate(action)
            }
        }

        binding.followersTextView.setOnClickListener {
            authManager.brokeAccessToken() //todo удалить
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
        }

        bindViewModel()

        binding.closeInfoBtn.setOnClickListener {
            binding.infoCardView.visibility = View.GONE
        }

        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) {
            if (it == false) {
                val athlete = viewModel.getAthleteFromCache()
                if (athlete != null) {
                    bindProfileInfo(athlete)
                    bindWeightView(athlete)
                    binding.logoutBtn.visibility = View.GONE
                    binding.infoCardView.visibility = View.VISIBLE
                }
            }
             else {
                binding.infoCardView.visibility = View.GONE
                binding.logoutBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun showLogoutDialog() {
        logoutDialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.logout_message))
            .setPositiveButton(getString(R.string.positiveBtn)) { _, _ ->
                clearLocalData()
            }
            .setNegativeButton(getString(R.string.negativeBtn), null)
            .show()
    }

    private fun clearLocalData() {
        viewModel.clearData()
        navController = activity?.findNavController(R.id.fragment)
        if (navController != null) {
            navController?.apply {
                this.navigate(R.id.authFragment)
                this.popBackStack(R.id.containerFragment, true)
            }
        }
    }

    private fun bindViewModel() {
        viewModel.getProfileInfo()

        viewModel.athlete.observe(viewLifecycleOwner) { athlete ->
            bindProfileInfo(athlete)
            bindWeightView(athlete)
            viewModel.putAthlete(athlete)
            //todo put athlete to db
        }

        viewModel.error.observe(viewLifecycleOwner) { t ->
            t.message?.let { toast(it) }
        }

        viewModel.clearData.observe(viewLifecycleOwner) {
            showLogoutDialog()
        }
    }

    private fun bindProfileInfo(athlete: Athlete) {
        binding.nameTextView.text = "${athlete.firstName} ${athlete.lastName}"
        binding.usernameTextView.text = "@ ${athlete.userName}"
        binding.followersCountTextView.text = athlete.followers?.toString() ?: "0"
        binding.followingCountTextView.text = athlete.friends?.toString() ?: "0"
        when (athlete.gender) {
            "M" -> binding.genderValue.text = MALE
            "F" -> binding.genderValue.text = FEMALE
            else -> binding.genderValue.text = OTHER
        }
        binding.countryValue.text = athlete.country

        Glide.with(this)
            .load(athlete.photoUrl)
            .placeholder(R.drawable.ic_placeholder)
            .fallback(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error_placeholder)
            .into(binding.athleteImageView)
    }

    private fun bindWeightView(
        athlete: Athlete
    ) {
        val list = (0..200).toList().map {
            "$it $KG"
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, list)
        binding.autoCompleteTextView.setAdapter(adapter)

        if (athlete.weight != null) {
            binding.autoCompleteTextView.setText(
                "${athlete.weight.toInt()} $KG", false
            )
        } else {
            binding.autoCompleteTextView.setText(getString(R.string.weight_not_selected), false)
        }

        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val newWeight = adapter.getItem(position)?.substringBefore(" $KG")?.toFloat()!!
            viewModel.changeAthleteWeight(UpdateRequestBody(newWeight))
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}