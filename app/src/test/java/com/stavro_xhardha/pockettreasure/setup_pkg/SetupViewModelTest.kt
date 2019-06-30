package com.stavro_xhardha.pockettreasure.setup_pkg

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.stavro_xhardha.pockettreasure.brain.observeOnce
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.ui.setup.SetupRepository
import com.stavro_xhardha.pockettreasure.ui.setup.SetupViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import retrofit2.Response
import java.lang.IllegalStateException


@RunWith(JUnit4::class)
class SetupViewModelTest {

    private lateinit var setupViewModel: SetupViewModel
    private val country = Country("Albania", "Tirana")
    private lateinit var setupRepository: SetupRepository
    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        //setup your dependencies, mocks and arrange
        setupRepository = mock()
        setupViewModel = SetupViewModel(setupRepository)

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun endTest() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        print("Test has finished")
    }

    @Test
    fun onCountrySelected_countryShouldBeCorrect() {

        setupViewModel.onCountrySelected(country)

        verify(setupRepository).saveCountryToSharedPreferences(country)
    }

    @Test
    fun showErrorLayout_shouldShowOnlyErrorVisible() {
        setupViewModel.showErrorLayout()

        assertError()
    }

    @Test
    fun switchProgressOf_shouldShowContent() {
        setupViewModel.showContent()

        assertContentVisible()
    }

    @Test
    fun switchProgressBarOn_shouldHideErrorAndContent() {
        setupViewModel.switchProgressBarOn()

        setupViewModel.pbVisibility.observeOnce {
            assertEquals(View.VISIBLE, it)
        }

        setupViewModel.errorVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }

        setupViewModel.contentVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun makeCountriesApiCall_shouldShowErrorWhenThrowingException() {
        runBlocking {
            `when`(setupRepository.makeCountryApiCallAsync()).then {
                throw IllegalStateException(" No Internet")
            }

            setupViewModel.makeCountriesApiCall()

            assertError()
        }
    }

    @Test
    fun makeCountriesApiCall_shouldShowErrorOnUnsuccessfulResponse() = runBlocking {
        `when`(setupRepository.makeCountryApiCallAsync()).thenReturn(
            Response.error(
                400, ResponseBody.create(
                    MediaType.parse("application/json"),
                    "{\"error_message\":[\"Do you even lift?\"]}"
                )
            )
        )

        setupViewModel.makeCountriesApiCall()

        assertError()
    }

    @Test
    fun makeCountriesApiCall_shouldShowMyList() = runBlocking {
        `when`(setupRepository.makeCountryApiCallAsync()).thenReturn(Response.success(arrayListOf(country)))

        setupViewModel.makeCountriesApiCall()

        assertContentVisible()
    }

    private fun assertContentVisible() {
        setupViewModel.pbVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }

        setupViewModel.errorVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }

        setupViewModel.contentVisibility.observeOnce {
            assertEquals(View.VISIBLE, it)
        }
    }

    private fun assertError() {
        setupViewModel.pbVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }

        setupViewModel.errorVisibility.observeOnce {
            assertEquals(View.VISIBLE, it)
        }

        setupViewModel.contentVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }
    }
}