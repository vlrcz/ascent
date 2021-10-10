package com.skillbox.ascentstrava.presentation.share

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.FragmentShareBinding

class ShareFragment : Fragment(R.layout.fragment_share) {

    private val binding: FragmentShareBinding by viewBinding(FragmentShareBinding::bind)
    private var contactListAdapter: ContactListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contactListAdapter = null
    }

    private fun initList() = with(binding.contactsList) {
        contactListAdapter = ContactListAdapter()
        adapter = contactListAdapter
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
    }
}