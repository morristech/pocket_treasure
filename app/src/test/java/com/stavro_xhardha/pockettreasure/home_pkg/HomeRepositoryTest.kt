package com.stavro_xhardha.pockettreasure.home_pkg

import com.nhaarman.mockitokotlin2.mock
import com.stavro_xhardha.pockettreasure.brain.GREGORIAN_DAY_KEY
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.home.HomeRepository
import com.stavro_xhardha.rocket.Rocket
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class HomeRepositoryTest {
    private lateinit var homeRepository: HomeRepository
    private lateinit var treasureApi: TreasureApi
    private lateinit var rocket: Rocket

    @Before
    fun setUp() {
        treasureApi = mock()
        rocket = mock()
        homeRepository = HomeRepository(treasureApi, rocket)
    }

    @After
    fun tearDown() {
        print("Test has finished")
    }

    @Test
    fun `zero gregorian day should return zero`() {
        `when`(rocket.readInt(GREGORIAN_DAY_KEY)).thenReturn(0)

        val day = homeRepository.getCurrentRegisteredDay()

        assertEquals(0, day)
    }

    @Test
    fun `number gregorian day should return the number`(){
        `when`(rocket.readInt(GREGORIAN_DAY_KEY)).thenReturn(55)

        val day = homeRepository.getCurrentRegisteredDay()

        assertEquals(55, day)
    }
}