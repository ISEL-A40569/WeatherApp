package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class Forecast (val name: String, val country: String, val lon: Float, val lat: Float, val dateTime: String,
                val list: Array<BasicWeatherInfo>) : CityInfo(
        name, country, lon, lat, list[0].dateTime){}