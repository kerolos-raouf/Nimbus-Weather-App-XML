package com.example.nimbusweatherapp.mainActivity

import app.cash.turbine.test
import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.data.database.FakeDatabaseHandler
import com.example.nimbusweatherapp.data.internetStateObserver.ConnectivityObserver
import com.example.nimbusweatherapp.data.internetStateObserver.FakeInternetStateObserver
import com.example.nimbusweatherapp.data.network.FakeNetworkHandler
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.data.repository.RepositoryImpl
import com.example.nimbusweatherapp.data.sharedPreference.FakeSharedPreferenceHandler
import com.example.nimbusweatherapp.favouriteFragment.FavouriteViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class SharedViewModelTest
{

    private lateinit var viewModel: SharedViewModel
    private lateinit var repository: Repository
    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var settingsHandler: SettingsHandler
    private lateinit var connectivityObserver: FakeInternetStateObserver


    @Before
    fun setUp()
    {
        connectivityObserver = FakeInternetStateObserver()
        remoteDataSource = FakeNetworkHandler()
        localDataSource = FakeDatabaseHandler()
        settingsHandler = FakeSharedPreferenceHandler()
        repository = RepositoryImpl(remoteDataSource, localDataSource, settingsHandler)
        viewModel = SharedViewModel(repository,connectivityObserver)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testOnInternetState_returnsAvailable() = runTest {
        viewModel.internetState.test {
            val result = awaitItem()
            assertEquals(ConnectivityObserver.InternetState.AVAILABLE, result)
        }
    }

    @Test
    fun setSharedPreferencesString_setValue() {
        //when
        viewModel.setSharedPreferencesString("key", "value")

        //then
        assertEquals("value", viewModel.getSharedPreferencesString("key"))
    }

    @Test
    fun setSharedPreferencesString_setEmptyValue() {
        //when
        viewModel.setSharedPreferencesString("key", "")

        //then
        assertEquals("", viewModel.getSharedPreferencesString("key"))
    }

    @Test
    fun setSharedPreferencesString_setNull() {
        //when
        viewModel.setSharedPreferencesString("key", null.toString())

        //then
        assertEquals("null", viewModel.getSharedPreferencesString("key"))
    }

    @Test
    fun setSharedPreferencesBoolean_setTure() {
        //when
        viewModel.setSharedPreferencesBoolean("key", true)
        //then
        assertEquals(true, viewModel.getSharedPreferencesBoolean("key"))
    }

    @Test
    fun setSharedPreferencesBoolean_setFalse() {
        //when
        viewModel.setSharedPreferencesBoolean("key", false)
        //then
        assertEquals(false, viewModel.getSharedPreferencesBoolean("key"))
    }

    @Test
    fun setSharedPreferencesString_withoutSettingValue() {
        //then
        assertEquals("", viewModel.getSharedPreferencesString("key"))
    }

    @Test
    fun setSharedPreferencesBoolean_withoutSettingValue() {
        //then
        assertEquals(false, viewModel.getSharedPreferencesBoolean("key"))
    }






}