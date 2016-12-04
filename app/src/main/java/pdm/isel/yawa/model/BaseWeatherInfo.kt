package pdm.isel.yawa.model

import android.graphics.Bitmap

abstract class BaseWeatherInfo(val date: String,
                               val description: String,
                               val pressure: String,
                               val humidity: String,
                               val icon: String
                               ) {
    var image: Bitmap? = null

}