package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class WeatherInfo (val name: String, val country: String, val lon: Float, val lat: Float, val dateTime: String,
                   val main: String, val description: String, val icon: String,
                   val temp: Float, val pressure: Float, val humidity: Float,
                   val windSpeed: Float, val windDeg: Float,
                   val sunrise: Long, val sunset: Long) : CityInfo(name, country, lon, lat, dateTime) {

}