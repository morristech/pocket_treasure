package com.stavro_xhardha.pockettreasure.setup_pkg

import com.nhaarman.mockitokotlin2.*
import com.stavro_xhardha.pockettreasure.brain.CAPITAL_SHARED_PREFERENCES_KEY
import com.stavro_xhardha.pockettreasure.brain.COUNTRIES_API_URL
import com.stavro_xhardha.pockettreasure.brain.COUNTRY_SHARED_PREFERENCE_KEY
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.setup.SetupRepository
import com.stavro_xhardha.rocket.Rocket
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import retrofit2.Response

@RunWith(JUnit4::class)
class SetupRepositoryTest {
    private lateinit var setupRepository: SetupRepository
    private lateinit var rocket: Rocket
    private lateinit var treasureApi: TreasureApi

    private val country = Country("Albania", "Tirana")

    @Before
    fun setUp() {
        rocket = mock()
        treasureApi = mock()

        setupRepository = SetupRepository(treasureApi, rocket)
    }

    @After
    fun finish() {
        println("Testing finished")
    }

    @Test
    fun `SetupRepository on given empty country and capital city, invoked method should return true`() {
        countryAndCapitalEmpty()

        val repositoryEmpty = setupRepository.isCountryOrCapitalEmpty()

        assertEquals(true, repositoryEmpty)
    }

    @Test
    fun `on country empty and capital not empty method should return true`() {
        countryEmptyCapitalNotEmpty()

        val repositoryEmpty = setupRepository.isCountryOrCapitalEmpty()

        assertEquals(true, repositoryEmpty)
    }

    @Test
    fun `on country not empty and capital empty method should return true`() {
        countryNotEmptyAndCapitalEmpty()

        val repositoryEmpty = setupRepository.isCountryOrCapitalEmpty()

        assertEquals(true, repositoryEmpty)
    }

    @Test
    fun `on both country and capital not empty method should return false`() {
        countryNotEmptyAndCapitalNotEmpty()

        val repositoryEmpty = setupRepository.isCountryOrCapitalEmpty()

        assertEquals(false, repositoryEmpty)
    }

    @Test
    fun `when writing country execution should go fine`() {
        val country = Country("Albania", "Tirana")
        setupRepository.saveCountryToSharedPreferences(country)

        verify(rocket).writeString(COUNTRY_SHARED_PREFERENCE_KEY, country.name)
        verify(rocket).writeString(CAPITAL_SHARED_PREFERENCES_KEY, country.capitalCity)
    }

    @Test
    fun `on api error response method should return response code 400`() = runBlocking {
        `when`(treasureApi.getCountriesListAsync(COUNTRIES_API_URL)).thenReturn(
            Response.error(
                400, ResponseBody.create(
                    MediaType.parse("application/json"),
                    "{\"error_message\":[\"Do you even lift?\"]}"
                )
            )
        )

        val apiResponse = setupRepository.makeCountryApiCallAsync()

        assertEquals(400, apiResponse.code())
    }

    @Test
    fun `on api success response method should return response code 200`() = runBlocking {
        `when`(treasureApi.getCountriesListAsync(COUNTRIES_API_URL)).thenReturn(Response.success(arrayListOf(country)))

        val apiResponse = setupRepository.makeCountryApiCallAsync()

        assertEquals(200, apiResponse.code())
        assertEquals(arrayListOf(country), apiResponse.body())
    }

    private fun countryNotEmptyAndCapitalEmpty() {
        `when`(rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)).thenReturn("Albania")
        `when`(rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)).thenReturn("")
    }

    private fun countryAndCapitalEmpty() {
        `when`(rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)).thenReturn("")
        `when`(rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)).thenReturn("")
    }

    private fun countryEmptyCapitalNotEmpty() {
        `when`(rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)).thenReturn("")
        `when`(rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)).thenReturn("Tirana")
    }

    private fun countryNotEmptyAndCapitalNotEmpty() {
        `when`(rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)).thenReturn("Albania")
        `when`(rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)).thenReturn("Tirana")
    }
}