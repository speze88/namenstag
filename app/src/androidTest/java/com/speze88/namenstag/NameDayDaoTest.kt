package com.speze88.namenstag

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.speze88.namenstag.data.local.NamenstagDatabase
import com.speze88.namenstag.data.local.dao.NameAliasDao
import com.speze88.namenstag.data.local.dao.NameDayDao
import com.speze88.namenstag.data.local.entity.NameAliasEntity
import com.speze88.namenstag.data.local.entity.NameDayEntity
import com.speze88.namenstag.data.local.entity.SaintEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NameDayDaoTest {

    private lateinit var database: NamenstagDatabase
    private lateinit var nameDayDao: NameDayDao
    private lateinit var nameAliasDao: NameAliasDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, NamenstagDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        nameDayDao = database.nameDayDao()
        nameAliasDao = database.nameAliasDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndQuerySaint() = runTest {
        val saint = SaintEntity(
            canonicalName = "Martin",
            description = "Martin von Tours war ein Bischof.",
            diedYear = 397,
            patronOf = "der Soldaten",
        )
        val id = nameDayDao.insertSaint(saint)
        val result = nameDayDao.getSaintById(id)
        assertNotNull(result)
        assertEquals("Martin", result!!.canonicalName)
    }

    @Test
    fun insertAndQueryNameDaysByDate() = runTest {
        val saint = SaintEntity(canonicalName = "Martin", description = "Test")
        val saintId = nameDayDao.insertSaint(saint)

        val nameDay = NameDayEntity(
            name = "Martin",
            month = 11,
            day = 11,
            saintId = saintId,
        )
        nameDayDao.insertNameDays(listOf(nameDay))

        val result = nameDayDao.getNameDaysByDate(11, 11).first()
        assertEquals(1, result.size)
        assertEquals("Martin", result[0].name)
    }

    @Test
    fun insertAndQueryAliases() = runTest {
        val aliases = listOf(
            NameAliasEntity("Hans", "Johannes"),
            NameAliasEntity("Hannes", "Johannes"),
        )
        nameAliasDao.insertAliases(aliases)

        val canonical = nameAliasDao.getCanonicalName("Hans")
        assertEquals("Johannes", canonical)

        val count = nameAliasDao.getAliasCount()
        assertEquals(2, count)
    }

    @Test
    fun getSaintsByDateReturnsCorrectSaints() = runTest {
        val saint1 = SaintEntity(canonicalName = "Martin", description = "Test1")
        val saint2 = SaintEntity(canonicalName = "Leopold", description = "Test2")
        val id1 = nameDayDao.insertSaint(saint1)
        val id2 = nameDayDao.insertSaint(saint2)

        nameDayDao.insertNameDays(
            listOf(
                NameDayEntity("Martin", 11, 11, id1),
                NameDayEntity("Leopold", 11, 15, id2),
            ),
        )

        val saints11 = nameDayDao.getSaintsByDate(11, 11).first()
        assertEquals(1, saints11.size)
        assertEquals("Martin", saints11[0].canonicalName)
    }
}
