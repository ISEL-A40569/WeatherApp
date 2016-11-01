package pdm.isel.yawa.model

/**
 * Created by Dani on 21-10-2016.
 */
class Forecast (val name: String,
                val country: String,
                val lon: Float,
                val lat: Float,
                val list: Array<FutureWeatherInfo>) :
        CityInfo(
            name, country, lon, lat){}