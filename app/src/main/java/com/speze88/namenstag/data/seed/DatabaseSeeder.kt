package com.speze88.namenstag.data.seed

import android.content.Context
import android.content.SharedPreferences
import com.speze88.namenstag.data.local.dao.NameAliasDao
import com.speze88.namenstag.data.local.dao.NameDayDao
import com.speze88.namenstag.data.local.entity.NameAliasEntity
import com.speze88.namenstag.data.local.entity.NameDayEntity
import com.speze88.namenstag.data.local.entity.SaintEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val nameDayDao: NameDayDao,
    private val nameAliasDao: NameAliasDao,
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val prefs: SharedPreferences =
        context.getSharedPreferences("namenstag_prefs", Context.MODE_PRIVATE)

    companion object {
        // Erhöhen wenn namedays_de.json geändert wird → erzwingt Re-Seed bei bestehenden Installationen
        const val SEED_VERSION = 2
        private const val KEY_SEED_VERSION = "seed_version"
    }

    suspend fun seedIfNeeded() {
        val storedVersion = prefs.getInt(KEY_SEED_VERSION, 0)
        if (storedVersion >= SEED_VERSION) return

        val seedData = loadSeedData()
        insertSeedData(seedData)

        prefs.edit().putInt(KEY_SEED_VERSION, SEED_VERSION).apply()
    }

    // Alias für Abwärtskompatibilität
    suspend fun seedIfEmpty() = seedIfNeeded()

    private fun loadSeedData(): SeedData {
        val jsonString = context.assets.open("namedays_de.json")
            .bufferedReader()
            .use { it.readText() }
        return json.decodeFromString<SeedData>(jsonString)
    }

    private suspend fun insertSeedData(seedData: SeedData) {
        for (seedSaint in seedData.saints) {
            val saintEntity = SaintEntity(
                canonicalName = seedSaint.canonicalName,
                description = seedSaint.description,
                bornYear = seedSaint.bornYear,
                diedYear = seedSaint.diedYear,
                patronOf = seedSaint.patronOf,
            )
            val saintId = nameDayDao.insertSaint(saintEntity)

            val nameDayEntities = seedSaint.nameDays.map { nameDay ->
                NameDayEntity(
                    name = seedSaint.canonicalName,
                    month = nameDay.month,
                    day = nameDay.day,
                    saintId = saintId,
                )
            }
            nameDayDao.insertNameDays(nameDayEntities)
        }

        val aliasEntities = seedData.aliases.map { alias ->
            NameAliasEntity(
                alias = alias.alias,
                canonicalName = alias.canonicalName,
            )
        }
        nameAliasDao.insertAliases(aliasEntities)
    }
}
