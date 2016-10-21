package pdm.isel.yawa.model

import java.util.*

/**
 * Created by Dani on 21-10-2016.
 */
class BasicWeatherInfo (val dateTime: Date, val tempMin: Float, val tempMax: Float,
                        val pressure: Float, val humidity: Float,
                        val main: String, val description: String, val icon: String) : Iconnable(icon) {}