package pdm.isel.yawa.model

import android.graphics.Bitmap

/**
 * Created by Dani on 01-11-2016.
 */
abstract class BaseWeatherInfo(val date: String,
                               val description: String,
                               val pressure: String,
                               val humidity: String,
                               val icon: String
                               ) {
    var image: Bitmap? = null

}