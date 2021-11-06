package com.skillbox.ascentstrava.presentation.athlete

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.data.AuthManager
import com.skillbox.ascentstrava.databinding.DialogCustomViewBinding
import com.skillbox.ascentstrava.databinding.FragmentProfileBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.athlete.data.UpdateRequestBody
import com.skillbox.ascentstrava.presentation.athlete.di.DaggerProfileComponent
import com.skillbox.ascentstrava.utils.toast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Provider


class AthleteFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        private const val MALE = "Male"
        private const val FEMALE = "Female"
        private const val OTHER = "Other"
        private const val KG = "kg"
        private const val LOGOUT_DIALOG = "logout_dialog"
    }

    @Inject
    lateinit var viewModelProvider: Provider<AthleteViewModel>

    @Inject
    lateinit var authManager: AuthManager // todo удалить

    private val viewModel: AthleteViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private var navController: NavController? = null

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
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

        bindViewModel()
        binding.shareBtn.setOnClickListener {
            val url = viewModel.getProfileUrl()
            if (url != null) {
                findNavController().navigate(
                    AthleteFragmentDirections.actionGlobalShareFragment(
                        url
                    )
                )
            }
        }

        binding.logoutBtn.setOnClickListener {
            showLogoutDialog()
        }

        binding.closeInfoBtn.setOnClickListener {
            binding.infoCardView.visibility = View.GONE
        }
    }

    private fun showLogoutDialog() {
        LogoutDialogFragment()
            .show(childFragmentManager, LOGOUT_DIALOG)
    }

    private fun bindViewModel() {

        viewModel.athlete.observe(viewLifecycleOwner) { athlete ->
            bindProfileInfo(athlete)
            bindWeightView(athlete)
        }

        viewModel.error.observe(viewLifecycleOwner) { t ->
            t.message?.let { toast(it) }
        }

        viewModel.clearData.observe(viewLifecycleOwner) {
            navController = activity?.findNavController(R.id.fragment)
            navController?.apply {
                this.navigate(R.id.authFragment)
                this.popBackStack(R.id.containerFragment, true)
            }
        }

        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) {
            if (!it) {
                binding.infoCardView.visibility = View.VISIBLE
                binding.logoutBtn.visibility = View.GONE
            } else {
                binding.infoCardView.visibility = View.GONE
                binding.logoutBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun bindProfileInfo(athlete: Athlete) {
        binding.nameTextView.text = athlete.fullName
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
}