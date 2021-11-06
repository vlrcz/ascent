package com.skillbox.ascentstrava.presentation.athlete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.DialogCustomViewBinding
import timber.log.Timber

class LogoutDialogFragment() : DialogFragment() {

    private val binding: DialogCustomViewBinding by viewBinding(DialogCustomViewBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_custom_view, container)
    }

    init {
        Timber.d("$viewModel")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.yesBtn.setOnClickListener {
            viewModel.logout()
        }
        binding.noBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }
}