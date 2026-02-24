package com.speze88.namenstag.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "name_days",
    primaryKeys = ["name", "month", "day", "language"],
    foreignKeys = [
        ForeignKey(
            entity = SaintEntity::class,
            parentColumns = ["id"],
            childColumns = ["saintId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["saintId"]),
        Index(value = ["month", "day"]),
    ],
)
data class NameDayEntity(
    val name: String,
    val month: Int,
    val day: Int,
    val saintId: Long,
    val language: String = "de",
)
