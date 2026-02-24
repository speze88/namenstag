package com.speze88.namenstag.data.repository

import com.speze88.namenstag.data.local.dao.NameAliasDao
import com.speze88.namenstag.data.local.dao.NameDayDao
import com.speze88.namenstag.data.seed.DatabaseSeeder
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.model.Saint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NameDayRepositoryImpl @Inject constructor(
    private val nameDayDao: NameDayDao,
    private val nameAliasDao: NameAliasDao,
    private val databaseSeeder: DatabaseSeeder,
) : NameDayRepository {

    override fun getNameDaysByDate(month: Int, day: Int): Flow<List<NameDay>> {
        return nameDayDao.getNameDaysByDate(month, day).map { entities ->
            entities.mapNotNull { entity ->
                val saint = nameDayDao.getSaintById(entity.saintId) ?: return@mapNotNull null
                NameDay(
                    name = entity.name,
                    month = entity.month,
                    day = entity.day,
                    saint = saint.toDomain(),
                )
            }
        }
    }

    override fun getNameDaysByName(name: String): Flow<List<NameDay>> {
        return nameDayDao.getNameDaysByName(name).map { entities ->
            entities.mapNotNull { entity ->
                val saint = nameDayDao.getSaintById(entity.saintId) ?: return@mapNotNull null
                NameDay(
                    name = entity.name,
                    month = entity.month,
                    day = entity.day,
                    saint = saint.toDomain(),
                )
            }
        }
    }

    override fun searchNameDays(query: String): Flow<List<NameDay>> {
        return nameDayDao.searchNameDays(query).map { entities ->
            entities.mapNotNull { entity ->
                val saint = nameDayDao.getSaintById(entity.saintId) ?: return@mapNotNull null
                NameDay(
                    name = entity.name,
                    month = entity.month,
                    day = entity.day,
                    saint = saint.toDomain(),
                )
            }
        }
    }

    override fun getSaintById(id: Long): Flow<Saint?> {
        return nameDayDao.getSaintByIdFlow(id).map { it?.toDomain() }
    }

    override suspend fun getAllNameDays(): List<NameDay> {
        return nameDayDao.getAllNameDays().mapNotNull { entity ->
            val saint = nameDayDao.getSaintById(entity.saintId) ?: return@mapNotNull null
            NameDay(
                name = entity.name,
                month = entity.month,
                day = entity.day,
                saint = saint.toDomain(),
            )
        }
    }

    override suspend fun getCanonicalName(alias: String): String? {
        return nameAliasDao.getCanonicalName(alias)
    }

    override suspend fun getAllCanonicalNames(): List<String> {
        return nameDayDao.getAllNameDays().map { it.name }.distinct()
    }

    override suspend fun seedDatabaseIfEmpty() {
        databaseSeeder.seedIfEmpty()
    }
}

private fun com.speze88.namenstag.data.local.entity.SaintEntity.toDomain() = Saint(
    id = id,
    canonicalName = canonicalName,
    description = description,
    bornYear = bornYear,
    diedYear = diedYear,
    patronOf = patronOf,
)
