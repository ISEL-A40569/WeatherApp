package pdm.isel.yawa.model

/**
 * Created by Dani on 21-10-2016.
 */
class CurrentWeatherInfo(val name: String, val country: String, val lon: Float, val lat: Float, val dateTime: String,
                         val main: String, val description: String, val icon: String,
                         val temp: Float, val pressure: Float, val humidity: Float,
                         val windSpeed: Float, val sunrise: Long, val sunset: Long, val _dt: Long) : CityInfo(name, country, lon, lat, dateTime) {

}