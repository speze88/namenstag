package com.speze88.namenstag.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "name_aliases",
    primaryKeys = ["alias", "canonicalName"],
)
data class NameAliasEntity(
    val alias: String,
    val canonicalName: String,
)
