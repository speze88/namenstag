package com.speze88.namenstag.data.contacts

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import com.speze88.namenstag.domain.model.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
            ContactsContract.Data.PHOTO_URI,
        )

        val selection = "${ContactsContract.Data.MIMETYPE} = ?"
        val selectionArgs = arrayOf(
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
        )

        val cursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${ContactsContract.Data.DISPLAY_NAME} ASC",
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
            val displayNameIndex = it.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)
            val givenNameIndex = it.getColumnIndex(
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
            )
            val photoUriIndex = it.getColumnIndex(ContactsContract.Data.PHOTO_URI)

            val seenIds = mutableSetOf<String>()

            while (it.moveToNext()) {
                val id = it.getString(idIndex) ?: continue
                if (!seenIds.add(id)) continue

                val displayName = it.getString(displayNameIndex) ?: continue
                val givenName = it.getString(givenNameIndex) ?: displayName.split(" ").first()
                val photoUri = it.getString(photoUriIndex)

                contacts.add(
                    Contact(
                        id = id,
                        displayName = displayName,
                        givenName = givenName,
                        photoUri = photoUri,
                    ),
                )
            }
        }

        return contacts
    }
}
