package com.speze88.namenstag.data.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedData(
    val saints: List<SeedSaint>,
    val aliases: List<SeedAlias>,
)

@Serializable
data class SeedSaint(
    val canonicalName: String,
    val description: String,
    val bornYear: Int? = null,
    val diedYear: Int? = null,
    val patronOf: String? = null,
    val nameDays: List<SeedNameDay>,
)

@Serializable
data class SeedNameDay(
    val month: Int,
    val day: Int,
)

@Serializable
data class SeedAlias(
    val alias: String,
    val canonicalName: String,
)
