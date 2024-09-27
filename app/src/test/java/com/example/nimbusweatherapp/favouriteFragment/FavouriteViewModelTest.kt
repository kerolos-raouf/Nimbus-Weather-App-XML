package com.example.nimbusweatherapp.favouriteFragment

import app.cash.turbine.test
import com.example.nimbusweatherapp.data.contracts.LocalDataSource
import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.contracts.SettingsHandler
import com.example.nimbusweatherapp.data.database.FakeDatabaseHandler
import com.example.nimbusweatherapp.data.model.FavouriteLocation
import com.example.nimbusweatherapp.data.network.FakeNetworkHandler
import com.example.nimbusweatherapp.data.repository.Repository
import com.example.nimbusweatherapp.data.repository.RepositoryImpl
import com.example.nimbusweatherapp.data.sharedPreference.FakeSharedPreferenceHandler
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class FavouriteViewModelTest
{
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var repository: Repository
    private lateinit var localDataSource: LocalDataSource
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var settingsHandler: SettingsHandler


    @Before
    fun setUp()
    {
        remoteDataSource = FakeNetworkHandler()
        localDataSource = FakeDatabaseHandler()
        settingsHandler = FakeSharedPreferenceHandler()
        repository = RepositoryImpl(remoteDataSource, localDataSource, settingsHandler)
        viewModel = FavouriteViewModel(repository)
    }


    @Test
    fun testFavouriteLocations_initialValueIsEmpty() = runTest {

        //Then
        assertEquals(0, viewModel.favouriteLocations.value.size)
    }


    @Test
    fun testOnAddFavouriteLocation_LocationIsAdded() = runTest {
        //Given
        val favouriteLocation = FavouriteLocation("Egypt", 35.6892, 51.3890)

        //When
        repository.insertFavouriteLocation(favouriteLocation)
        viewModel.favouriteLocations.test {


            val result = awaitItem()
            assertEquals(0, result.size)

            repository.insertFavouriteLocation(favouriteLocation)


            val updatedResult = awaitItem()
            assertEquals(1, updatedResult.size)
           // assertEquals(favouriteLocation, result[0])
        }
    }

    @Test
    fun testOnDeleteFavouriteLocation_LocationIsDeleted() = runTest {
        //Given
        val favouriteLocation = FavouriteLocation("Egypt", 35.6892, 51.3890)
        repository.insertFavouriteLocation(favouriteLocation)

        //When
        viewModel.deleteFavouriteLocation(favouriteLocation)
        viewModel.favouriteLocations.test {
            val result = awaitItem()
            assertEquals(0, result.size)
        }
    }
}