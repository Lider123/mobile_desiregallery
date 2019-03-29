package com.example.desiregallery

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*


class UtilsUnitTest {
    private lateinit var date: Date

    @Before
    fun initDate() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2012)
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 21)
        calendar.set(Calendar.HOUR_OF_DAY, 1)
        calendar.set(Calendar.MINUTE, 23)
        calendar.set(Calendar.SECOND, 51)
        date = calendar.time
    }

    @Test
    fun dateToString_isCorrect() {
        Assert.assertEquals("2012-12-21 01:23:51", Utils.dateToString(date))
    }

    @Test
    fun stringToDate_isCorrect() {
        Assert.assertTrue(Utils.stringToDate("2012-12-21 01:23:51")!!.compareTo(date) < 0)
    }
}