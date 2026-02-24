package com.speze88.namenstag.data.repository

import com.speze88.namenstag.domain.model.Contact

interface ContactRepository {
    suspend fun getContacts(): List<Contact>
}
