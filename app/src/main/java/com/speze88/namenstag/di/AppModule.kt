package com.speze88.namenstag.di

import android.content.Context
import androidx.room.Room
import com.speze88.namenstag.data.local.NamenstagDatabase
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

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NamenstagDatabase {
        return Room.databaseBuilder(
            context,
            NamenstagDatabase::class.java,
            "namenstag.db",
        ).build()
    }

    @Provides
    fun provideNameDayDao(database: NamenstagDatabase): NameDayDao {
        return database.nameDayDao()
    }

    @Provides
    fun provideNameAliasDao(database: NamenstagDatabase): NameAliasDao {
        return database.nameAliasDao()
    }
}
