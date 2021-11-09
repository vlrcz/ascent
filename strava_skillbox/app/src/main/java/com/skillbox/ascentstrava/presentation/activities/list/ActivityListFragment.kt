package com.skillbox.ascentstrava.presentation.activities.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.app.appComponent
import com.skillbox.ascentstrava.databinding.FragmentActivitiesBinding
import com.skillbox.ascentstrava.di.ViewModelFactory
import com.skillbox.ascentstrava.presentation.activities.list.di.DaggerActivitiesComponent
import com.skillbox.ascentstrava.utils.toast
import javax.inject.Inject
import javax.inject.Provider

class ActivityListFragment : Fragment(R.layout.fragment_activities) {

    private val binding: FragmentActivitiesBinding by viewBinding(FragmentActivitiesBinding::bind)

    @Inject
    lateinit var viewModelProvider: Provider<ActivityListViewModel>

    private val viewModel: ActivityListViewModel by viewModels { ViewModelFactory { viewModelProvider.get() } }

    private val activityListAdapter = ActivityListAdapter() { url ->
        findNavController().navigate(ActivityListFragmentDirections.actionGlobalShareFragment(url))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerActivitiesComponent
            .factory()
            .create(context.appComponent)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        bindViewModel()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_activitiesFragment_to_createActivityFragment)
        }

        binding.pullToRefresh.setOnRefreshListener {
            viewModel.loadList()
            binding.pullToRefresh.isRefreshing = false
        }

        binding.closeInfoBtn.setOnClickListener {
            binding.infoCardView.visibility = View.GONE
        }
    }

    private fun bindViewModel() {
        viewModel.loadList()

        viewModel.activitiesLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                activityListAdapter.submitList(it)
                binding.emptyListTextView.visibility = View.GONE
            } else {
                binding.emptyListTextView.visibility = View.VISIBLE
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            toast(it)
        }

        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) {
            if (it) {
                binding.infoCardView.visibility = View.GONE
            } else {
                binding.infoCardView.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner, ::updateLoadingState)
    }

    private fun initList() {
        with(binding.activitiesList) {
            adapter = activityListAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            scrollToPosition(0)
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        binding.activitiesList.isVisible = isLoading.not()
        binding.progressBar.isVisible = isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.activitiesList.adapter = null
    }
}