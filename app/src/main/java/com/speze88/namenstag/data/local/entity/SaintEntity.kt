package com.speze88.namenstag.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saints")
data class SaintEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val canonicalName: String,
    val description: String,
    val bornYear: Int? = null,
    val diedYear: Int? = null,
    val patronOf: String? = null,
    val language: String = "de",
)
