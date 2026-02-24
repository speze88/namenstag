package com.speze88.namenstag.domain.model

data class Contact(
    val id: String,
    val displayName: String,
    val givenName: String,
    val photoUri: String? = null,
)
