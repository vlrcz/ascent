package com.skillbox.ascentstrava.presentation.share.data

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepository @Inject constructor(private val context: Context) {

    suspend fun getAllContacts(): List<Contact> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )?.use {
            getContactsFromCursor(it)
        }.orEmpty()
    }

    private fun getContactsFromCursor(cursor: Cursor): List<Contact> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<Contact>()
        do {
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val name = cursor.getString(nameIndex).orEmpty()

            val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
            val id = cursor.getLong(idIndex)

            val phoneId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val phone = cursor.getString(phoneId)

            val imageId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)
            val imageUri = cursor.getString(imageId)

            val contact = Contact(
                id = id,
                name = name,
                phone = phone,
                imageUri = imageUri
            )
            list.add(contact)
        } while (cursor.moveToNext())

        return list
    }
}