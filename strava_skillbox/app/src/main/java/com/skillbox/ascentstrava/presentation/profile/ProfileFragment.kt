package com.skillbox.ascentstrava.presentation.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.skillbox.ascentstrava.utils.toast
import javax.inject.Inject
import javax.inject.Provider

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        private const val SMS_BODY = "sms_body"
        private const val MALE = "Male"
        private const val FEMALE = "Female"
        private const val OTHER = "Other"
        private const val KG = "kg"
    }

    @Inject
    lateinit var viewModelProvider: Provider<ProfileViewModel>

    @Inject
    lateinit var authManager: AuthManager

    private val viewModel: ProfileViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    private var rationaleDialog: AlertDialog? = null

    private var profileUrl: String? = null

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val contentUri = result.data?.data
                val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (contentUri != null) {
                    requireContext().contentResolver.query(contentUri, projection, null, null, null)
                        .use { cursor ->
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {
                                    val numberIndex =
                                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    val number = cursor.getString(numberIndex)

                                    sendSmsToContact(number) //todo перенести обращение к contentResolver в репозиторий
                                }
                            }
                        }
                } else {
                    toast(R.string.no_contact_selected)
                }
            } else {
                toast(R.string.call_canceled)
            }
        }

    private val requestContactsReadResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openContactsBook()
            } else {
                val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_CONTACTS
                )
                if (needRationale) {
                    showRationaleDialog()
                }
            }
        }

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

        binding.shareBtn.setOnClickListener {
            shareProfileWithCheckPermission()
        }

        binding.followersTextView.setOnClickListener {
            authManager.brokeAccessToken() //todo удалить
        }

        bindViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rationaleDialog?.dismiss()
        rationaleDialog = null
    }

    private fun shareProfileWithCheckPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openContactsBook()
        } else {
            val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_CONTACTS
            )
            if (needRationale) {
                showRationaleDialog()
            } else {
                requestContactsReadPermission()
            }
        }
    }

    private fun showRationaleDialog() {
        rationaleDialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.need_contacts_read_permission))
            .setPositiveButton(getString(R.string.positiveBtn)) { _, _ -> requestContactsReadPermission() }
            .setNegativeButton(getString(R.string.negativeBtn), null)
            .show()
    }

    private fun requestContactsReadPermission() {
        requestContactsReadResultLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun openContactsBook() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
        activityResultLauncher.launch(intent)
    }

    private fun sendSmsToContact(number: String?) {
        val uri = Uri.parse("smsto:$number")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra(SMS_BODY, getString(R.string.already_in_strava) + " " + profileUrl)
        startActivity(intent)
    }

    private fun bindViewModel() {
        viewModel.getProfileInfo()

        viewModel.athlete.observe(viewLifecycleOwner) { athlete ->
            bindProfileInfo(athlete)
            bindWeightView(athlete)
            profileUrl = getString(R.string.profile_url) + athlete.userName
        }

        viewModel.error.observe(viewLifecycleOwner) { t ->
            t.message?.let { toast(it) }
        }
    }

    @SuppressLint("SetTextI18n")
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
            .error(R.drawable.ic_error_placeholder)
            .into(binding.imageView)
    }

    @SuppressLint("SetTextI18n")
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