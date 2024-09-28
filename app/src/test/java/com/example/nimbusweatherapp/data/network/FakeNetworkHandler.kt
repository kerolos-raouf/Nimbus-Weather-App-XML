package com.example.nimbusweatherapp.data.network

import com.example.nimbusweatherapp.data.contracts.RemoteDataSource
import com.example.nimbusweatherapp.data.model.CitiesForSearch
import com.example.nimbusweatherapp.data.model.City
import com.example.nimbusweatherapp.data.model.Clouds
import com.example.nimbusweatherapp.data.model.Coord
import com.example.nimbusweatherapp.data.model.LocationNameResponse
import com.example.nimbusweatherapp.data.model.Main
import com.example.nimbusweatherapp.data.model.Weather
import com.example.nimbusweatherapp.data.model.WeatherEveryThreeHours
import com.example.nimbusweatherapp.data.model.WeatherForLocation
import com.example.nimbusweatherapp.data.model.WeatherItemEveryThreeHours
import com.example.nimbusweatherapp.data.model.Wind
import com.example.nimbusweatherapp.utils.Constants
import kotlinx.coroutines.delay
import retrofit2.Response

class FakeNetworkHandler : RemoteDataSource {


    private val weatherEveryThreeHours = WeatherEveryThreeHours(
        list = listOf(
            WeatherItemEveryThreeHours(
                dt = 1643723400,
                main = Main(
                    temp = 280.07,
                    feelsLike = 278.88,
                    tempMin = 280.07,
                    tempMax = 280.07,
                    pressure = 1019,
                    seaLevel = 1019,
                    grndLevel = 1014,
                    humidity = 68
                ),
                weather = listOf(
                    Weather(
                        id = 500,
                        main = "Rain",
                        description = "clear sky",
                        icon = "10d"
                    )
                ),
                clouds = Clouds(all = 100),
                wind = Wind(speed = "2.64", deg = 300, gust = 2.64),
                visibility = 10000,
                pop = 0.87,
                textDataTime = "2022-02-22 15:00:00",
            )
        ),
        city = City(
            id = 2643743,
            name = "London",
            coord = Coord(
                lat = 51.5073,
                lon = -0.1277
            ),
            country = "GB",
            population = 1000000,
            timezone = 0,
            sunrise = 1643699344,
            sunset = 1643734391
        ),
        message = 0,
        cod = "200",
        cnt = 1
    )




    override suspend fun getWeatherEveryThreeHours(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Response<WeatherEveryThreeHours> {


        if(language == "ar")
        {
            weatherEveryThreeHours.list[0].weather[0].description = "سماء صافية"
        }
        if (units == Constants.METRIC)
        {
            weatherEveryThreeHours.list[0].main.temp = 300.0
        }else if (units == Constants.IMPERIAL)
        {
            weatherEveryThreeHours.list[0].main.temp = 90.0
        }
        return Response.success(weatherEveryThreeHours)
    }

    override suspend fun getWeatherForLocation(
        latitude: Double,
        longitude: Double,
        language: String,
        units: String
    ): Response<WeatherForLocation> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocationInfoUsingCoordinates(
        latitude: Double,
        longitude: Double
    ): Response<LocationNameResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocationInfoUsingName(locationName: String): Response<LocationNameResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherByCountryName(
        countryName: String,
        language: String,
        units: String
    ): Response<WeatherForLocation> {
        TODO("Not yet implemented")
    }

    override suspend fun getCitiesListForSearch(name: String): Response<CitiesForSearch> {
        TODO("Not yet implemented")
    }
}