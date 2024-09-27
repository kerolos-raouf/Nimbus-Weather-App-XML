package com.example.nimbusweatherapp.data.repository

import app.cash.turbine.test
import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.data.database.FakeDatabaseHandler
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.network.FakeNetworkHandler
import com.example.nimbusweatherapp.data.sharedPreference.FakeSharedPreferenceHandler
import com.example.nimbusweatherapp.utils.Constants
import com.example.nimbusweatherapp.utils.State
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@FlowPreview
class RepositoryImplTest{

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource
    private lateinit var settingsHandler: SettingsHandler
    private lateinit var repository: Repository

    @Before
    fun setUp()
    {
        remoteDataSource = FakeNetworkHandler()
        localDataSource = FakeDatabaseHandler()
        settingsHandler = FakeSharedPreferenceHandler()
        repository = RepositoryImpl(remoteDataSource, localDataSource, settingsHandler)
    }

    @Test
    fun getWeatherEveryThreeHours_arabicLanguage_returnsClearSkyInArabic() = runTest{
        //when
        val response = repository.getWeatherEveryThreeHours(0.0,0.0,"ar", Constants.METRIC)
        //then
        response.test {

            assertEquals(State.Loading, awaitItem())

            val successState = awaitItem()
            assert(successState is State.Success)
            val data = (successState as State.Success).data

            assertEquals(1, data.list.size)
            assertEquals("سماء صافية", data.list[0].weather[0].description)

            awaitComplete()
        }
    }

    @Test
    fun getWeatherEveryThreeHours_englishLanguage_returnsClearSkyInEnglish() = runTest{
        //when
        val response = repository.getWeatherEveryThreeHours(0.0,0.0,"en", Constants.METRIC)
        //then
        response.test {

            assertEquals(State.Loading, awaitItem())

            val successState = awaitItem()
            assert(successState is State.Success)
            val data = (successState as State.Success).data

            assertEquals(1, data.list.size)
            assertEquals("clear sky", data.list[0].weather[0].description)

            awaitComplete()
        }
    }

    @Test
    fun getWeatherEveryThreeHours_imperialUint_returnsTemperatureInFahrenheit() = runTest{
        //when
        val response = repository.getWeatherEveryThreeHours(0.0,0.0,"en", Constants.IMPERIAL)
        //then
        response.test {

            assertEquals(State.Loading, awaitItem())

            val successState = awaitItem()
            assert(successState is State.Success)
            val data = (successState as State.Success).data

            assertEquals(1, data.list.size)
            assertEquals(90.0, data.list[0].main.temp)

            awaitComplete()
        }
    }

    @Test
    fun getWeatherEveryThreeHours_metricUint_returnsTemperatureInCelsius() = runTest{
        //when
        val response = repository.getWeatherEveryThreeHours(0.0,0.0,"en", Constants.METRIC)
        //then
        response.test {

            assertEquals(State.Loading, awaitItem())

            val successState = awaitItem()
            assert(successState is State.Success)
            val data = (successState as State.Success).data

            assertEquals(1, data.list.size)
            assertEquals(300.0, data.list[0].main.temp)

            awaitComplete()
        }
    }




//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun getWeatherEveryThreeHours_TimeOut_Exception() = runTest{
//        //when
//        val response = repository.getWeatherEveryThreeHours(0.0,0.0,"en", Constants.IMPERIAL)
//
//        //then
//        response.test {
//            assertEquals(State.Loading, awaitItem())
//
//            advanceTimeBy(12000)
//
//            val errorState = awaitItem()
//            assert(errorState is State.Error)
//            val msg = (errorState as State.Error).message
//
//            assertEquals("Time out", msg)
//
//            awaitComplete()
//        }
//    }


    @Test
    fun insertFavouriteLocation_returnsSizeOne() = runTest{
        //given
        val favouriteLocation = FavouriteLocation("Egypt", 35.6892, 51.3890)
        //when
        repository.insertFavouriteLocation(favouriteLocation)

        //then
        assertEquals(1, (localDataSource as FakeDatabaseHandler).favouriteLocations.value.size)
    }

    @Test
    fun getSharedPreferencesBoolean_isNotificationEnabled_returnsTrue()
    {
        //when
        val result = repository.getSharedPreferencesBoolean(Constants.NOTIFICATION_KEY)
        //then
        assertEquals(true, result)
    }

    @Test
    fun getSharedPreferencesBoolean_defaultValue_returnsFalse()
    {
        //when
        val result = repository.getSharedPreferencesBoolean("This is not a key")
        //then
        assertEquals(false, result)
    }

}