package com.cs.weatherapplication;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.intellij.lang.annotations.Pattern;

import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(binding.root);
        fetchWeatherData("Horana South")
        SearchCity()
        }

    Private fun SearchCity() {
        val searchview = binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
               if (query != null) {
                   fetchWeatherData(query)
               }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
    private fun fetchWeatherData(cityName:String) {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(city: cityName, apiid: "fb4cb12a16f9e50203d82f5b6d11dd66" , units : "metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call:Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.Body()
                if (response.isSuccessful && responseBody != null){
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val condition = responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    binding.temp.text= "$temperature °C"
                    binding.weather.text = condition
                    binding.maxTemp.text =  "Max Temp: $maxTemp °C"
                    binding.minTemp.text =  "Min Temp: $minTemp °C"
                    binding.humidity.text = "$humidity %"
                    binding.windSpeed.text = "$windSpeed m/s"
                    binding.sunRise.text = "${time(sunRise)}"
                    binding.sunSet.text = "${time(sunSet)}"
                    binding.condition.text = condition
                    binding.day.text =dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.cityName.text = "$cityName"
                //Log.d("TAG, "onResponse: $temperature")


                    }
                }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
                }
            })
    }

        fun dayName(timestmp: long): String{
            val sdf = SimpleDateFormat("EEEE", locale.getDefault())
            return sdf.format((Date()))
        }
}
    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat(: "HH:mm", locale.getDefault())
        return sdf.format((Date(date: timestamp*1000)))
    }
    private fun date(): String {
        val sdf = SimpleDateFormat(: "dd MMMM yyyy", locale.getDefault())
        return sdf.format((Date()))
    }
}