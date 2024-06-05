package com.cs.weatherapplication

import retrofit2.http.GET

interface ApiInterface {
    @GET("weather")
    fun getWeatherData(
            @Query("q") city:String,
            @Query("appid") appid:String,
            @Query("units") units:String
    ) : call<WeatherApp>
}