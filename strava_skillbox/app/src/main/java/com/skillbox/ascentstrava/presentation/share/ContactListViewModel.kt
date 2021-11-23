package com.skillbox.ascentstrava.presentation.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillbox.ascentstrava.presentation.share.data.Contact
import com.skillbox.ascentstrava.presentation.share.data.ContactsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    private val contactsMutableLiveData = MutableLiveData<List<Contact>>()

    val contactsLiveData: LiveData<List<Contact>>
        get() = contactsMutableLiveData

    fun loadList() {
        viewModelScope.launch {
            try {
                val contactsList = contactsRepository.getAllContacts()
                contactsMutableLiveData.postValue(contactsList)
            } catch (t: Throwable) {
                contactsMutableLiveData.postValue(emptyList())
            }
        }
    }
}