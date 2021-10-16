package com.skillbox.ascentstrava.presentation.share

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.FragmentShareBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.profile.ProfileFragment
import com.skillbox.ascentstrava.presentation.profile.ProfileViewModel
import com.skillbox.ascentstrava.presentation.share.di.DaggerShareComponent
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class ShareFragment : Fragment(R.layout.fragment_share) {

    companion object {
        private const val SMS_BODY = "sms_body"
    }

    @Inject
    lateinit var viewModelProvider: Provider<ContactListViewModel>

    private val viewModel: ContactListViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private val binding: FragmentShareBinding by viewBinding(FragmentShareBinding::bind)

    private var contactListAdapter: ContactListAdapter? = null

    private var rationaleDialog: AlertDialog? = null

    private val args: ShareFragmentArgs by navArgs()

    private val requestContactsReadResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.loadList()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        bindViewModel()
        requestContactsReadPermission()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerShareComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contactListAdapter = null
        rationaleDialog?.dismiss()
        rationaleDialog = null
    }

    private fun requestContactsReadPermission() {
        requestContactsReadResultLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun bindViewModel() {
        viewModel.contactsLiveData.observe(viewLifecycleOwner) {
            contactListAdapter?.submitList(it)
        }
    }

    private fun initList() {
        with(binding.contactsList) {
            contactListAdapter = ContactListAdapter() { contact ->
                contact.phone?.let { shareProfileWithCheckPermission(it) }
            }
            adapter = contactListAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun shareProfileWithCheckPermission(phoneNumber: String) {
        if (isReadContactsGranted()) {
            sendSmsToContact(phoneNumber)
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

    private fun isReadContactsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showRationaleDialog() {
        rationaleDialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.need_contacts_read_permission))
            .setPositiveButton(getString(R.string.positiveBtn)) { _, _ -> requestContactsReadPermission() }
            .setNegativeButton(getString(R.string.negativeBtn), null)
            .show()
    }

    private fun sendSmsToContact(number: String?) {
        val uri = Uri.parse("smsto:$number")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        val url = args.url
        if (url.contains("athletes")) {
            startSmsActivity(intent, R.string.already_in_strava)
        }
        if (url.contains("activities")) {
            startSmsActivity(intent, R.string.rate_my_activity)
        }
    }

    private fun startSmsActivity(intent: Intent, @StringRes stringRes: Int) {
        intent.putExtra(
            SMS_BODY,
            getString(stringRes) + " " + args.url
        )
        startActivity(intent)
    }
}