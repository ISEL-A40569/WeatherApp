package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class Current(val name: String,
              val country: String,
              val lon: Float,
              val lat: Float,
              val currentInfo: CurrentWeatherInfo
              ) : CityInfo(name, country, lon, lat) {

}