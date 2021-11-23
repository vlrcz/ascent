package com.skillbox.ascentstrava.presentation.athlete

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.DialogCustomViewBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.athlete.di.DaggerProfileComponent
import javax.inject.Inject
import javax.inject.Provider

class LogoutDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelProvider: Provider<AthleteViewModel>

    private val viewModel: AthleteViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private val binding: DialogCustomViewBinding by viewBinding(DialogCustomViewBinding::bind)

    private var navController: NavController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_custom_view, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.yesBtn.setOnClickListener {
            viewModel.logout()
        }
        binding.noBtn.setOnClickListener {
            dismiss()
        }

        viewModel.clearData.observe(viewLifecycleOwner) {
            navController = activity?.findNavController(R.id.fragment)
            navController?.apply {
                this.navigate(R.id.authFragment)
                this.popBackStack(R.id.containerFragment, true)
            }

            dismiss()
        }
    }
}