package com.speze88.namenstag.domain.model

data class Saint(
    val id: Long,
    val canonicalName: String,
    val description: String,
    val bornYear: Int? = null,
    val diedYear: Int? = null,
    val patronOf: String? = null,
)
