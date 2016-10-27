package pdm.isel.yawa.model

/**
 * Created by Dani on 21-10-2016.
 */
class FutureWeatherInfo(val dateTime: String, val tempMin: Float, val tempMax: Float,
                        val pressure: Float, val humidity: Float,
                        val main: String, val description: String, val icon: String, val _dt: Long) {

}