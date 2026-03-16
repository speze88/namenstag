package com.speze88.namenstag.di

import android.content.Context
import androidx.room.migration.Migration
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.speze88.namenstag.data.local.NamenstagDatabase
import com.speze88.namenstag.data.local.dao.ContactNameDayPreferenceDao
import com.speze88.namenstag.data.local.dao.NameAliasDao
import com.speze88.namenstag.data.local.dao.NameDayDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `contact_nameday_preferences` (
                    `contactId` TEXT NOT NULL,
                    `saintId` INTEGER NOT NULL,
                    `month` INTEGER NOT NULL,
                    `day` INTEGER NOT NULL,
                    PRIMARY KEY(`contactId`)
                )
                """.trimIndent(),
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NamenstagDatabase {
        return Room.databaseBuilder(
            context,
            NamenstagDatabase::class.java,
            "namenstag.db",
        ).addMigrations(migration1To2).build()
    }

    @Provides
    fun provideNameDayDao(database: NamenstagDatabase): NameDayDao {
        return database.nameDayDao()
    }

    @Provides
    fun provideNameAliasDao(database: NamenstagDatabase): NameAliasDao {
        return database.nameAliasDao()
    }

    @Provides
    fun provideContactNameDayPreferenceDao(database: NamenstagDatabase): ContactNameDayPreferenceDao {
        return database.contactNameDayPreferenceDao()
    }
}
