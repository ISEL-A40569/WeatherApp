package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class CurrentWeatherInfo(val name: String,
                         val country: String,
                         val lon: Float,
                         val lat: Float,
                         val dateTime: String,
                         val main: String,
                         val description: String,
                         val icon: String,
                         val temp: Float,
                         val pressure: Float,
                         val humidity: Float,
                         val windSpeed: Float,
                         val sunrise: Long,
                         val sunset: Long,
                         val _dt: Long) : CityInfo(name, country, lon, lat, dateTime) {



    private fun formatDateValue (dateValue: Long) : String {
        return Date(dateValue * 1000).toString()
    }


    public fun getSunset(): String{
        var date:String = formatDateValue(sunset)
        var d = date.split(" ")
        return d[3]
    }

    public fun getSunrise(): String{
        var date:String = formatDateValue(sunrise)
        var d = date.split(" ")
        return d[3]
    }

    public fun getDate():String{

        var d = dateTime.split(" ")
        return d[2] + " " + d[1] + " "+ d[5]
    }
}