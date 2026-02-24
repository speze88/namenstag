package com.speze88.namenstag.data.repository

import com.speze88.namenstag.data.contacts.ContactsDataSource
import com.speze88.namenstag.domain.model.Contact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactsDataSource: ContactsDataSource,
) : ContactRepository {

    override suspend fun getContacts(): List<Contact> {
        return contactsDataSource.getContacts()
    }
}
