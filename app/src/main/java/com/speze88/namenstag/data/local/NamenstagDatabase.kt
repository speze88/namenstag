package com.speze88.namenstag.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.speze88.namenstag.data.local.converter.DateConverters
import com.speze88.namenstag.data.local.dao.ContactNameDayPreferenceDao
import com.speze88.namenstag.data.local.dao.NameAliasDao
import com.speze88.namenstag.data.local.dao.NameDayDao
import com.speze88.namenstag.data.local.entity.ContactNameDayPreferenceEntity
import com.speze88.namenstag.data.local.entity.NameAliasEntity
import com.speze88.namenstag.data.local.entity.NameDayEntity
import com.speze88.namenstag.data.local.entity.SaintEntity

@Database(
    entities = [
        NameDayEntity::class,
        SaintEntity::class,
        NameAliasEntity::class,
        ContactNameDayPreferenceEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(DateConverters::class)
abstract class NamenstagDatabase : RoomDatabase() {
    abstract fun nameDayDao(): NameDayDao
    abstract fun nameAliasDao(): NameAliasDao
    abstract fun contactNameDayPreferenceDao(): ContactNameDayPreferenceDao
}
