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
            ContactsContract.Contacts.CONTENT_URI,
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
            val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
            val name = cursor.getString(nameIndex).orEmpty()

            val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val id = cursor.getLong(idIndex)

            val phone = if (getPhoneForContact(id).isEmpty()) {
                ""
            } else {
                getPhoneForContact(id)
            }

            val imageUri = if (getImageUriForContact(id).isEmpty()) {
                ""
            } else {
                getImageUriForContact(id)
            }

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

    private fun getImageUriForContact(contactId: Long): String {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getImageUriFromCursor(it)
        }.orEmpty()
    }

    private fun getImageUriFromCursor(cursor: Cursor): String {
        return if (cursor.moveToFirst()) {
            val numberIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)
            cursor.getString(numberIndex)
        } else {
            ""
        }
    }

    private fun getPhoneForContact(contactId: Long): String {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use {
            getPhoneFromCursor(it)
        }.orEmpty()
    }

    private fun getPhoneFromCursor(cursor: Cursor): String {
        return if (cursor.moveToFirst()) {
            val numberIndex =
                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            cursor.getString(numberIndex)
        } else {
            ""
        }
    }
}